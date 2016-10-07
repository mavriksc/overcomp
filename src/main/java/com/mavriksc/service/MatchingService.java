package com.mavriksc.service;

import static org.bytedeco.javacpp.opencv_core.CV_32FC1;
import static org.bytedeco.javacpp.opencv_core.CV_8UC1;
import static org.bytedeco.javacpp.opencv_core.FONT_HERSHEY_PLAIN;
import static org.bytedeco.javacpp.opencv_imgcodecs.*;
import static org.bytedeco.javacpp.opencv_imgproc.COLOR_BGR2GRAY;
import static org.bytedeco.javacpp.opencv_imgproc.TM_CCORR_NORMED;
import static org.bytedeco.javacpp.opencv_imgproc.cvtColor;
import static org.bytedeco.javacpp.opencv_imgproc.matchTemplate;
import static org.bytedeco.javacpp.opencv_imgproc.putText;
import static org.bytedeco.javacpp.opencv_imgproc.resize;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Point;
import org.bytedeco.javacpp.opencv_core.Rect;
import org.bytedeco.javacpp.opencv_core.Scalar;
import org.bytedeco.javacpp.opencv_core.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mavriksc.types.CharGuess;
import com.mavriksc.types.CharacterGuesses;
import com.mavriksc.types.Teams;


public class MatchingService {	
	
	private static final Logger log = LoggerFactory.getLogger(MatchingService.class);
	private static final String TEMPLATES_FOLDER = "static/ow-templates/";
	private static final String IMG_PROC_PATH = "img-proc/";
	private static final Size defaultScreenshotSize = new Size(1920, 1080);
	private static List<String> characters;	
	
	
	public static void scaleAndCheckAll(String guid){		
		Mat source = imread(IMG_PROC_PATH + guid);	
		Mat scaledSrc = new Mat(defaultScreenshotSize, source.type());
		resize(source, scaledSrc, defaultScreenshotSize);
		Mat sourceGrey = new Mat(scaledSrc.size(), CV_8UC1);
		cvtColor(scaledSrc, sourceGrey, COLOR_BGR2GRAY);		
		
		for (String hero : getCharacters()) {
			Mat template = OpenCVUtils.matFromJar(TEMPLATES_FOLDER + hero + ".png", 0);
			Size size = new Size(sourceGrey.cols()-template.cols()+1, sourceGrey.rows()-template.rows()+1);
			Mat result = new Mat(size, CV_32FC1);
			matchTemplate(sourceGrey, template, result, TM_CCORR_NORMED);
			Scalar color =  OpenCVUtils.randColor();
			List<Point> points = OpenCVUtils.getPointsFromMatAboveThreshold(result, 0.90f);
			for (Point point : points) {
				//rectangle(scaledSrc, new Rect(point.x(),point.y(),template.cols(),template.rows()), color, -2, 0, 0);
				putText(scaledSrc, hero, point, FONT_HERSHEY_PLAIN, 2, color);
			}
		}
		String withExt = IMG_PROC_PATH + guid +".png";
		imwrite(withExt,  scaledSrc);
		File noExt = new File(IMG_PROC_PATH + guid);
		File ext = new File(withExt);
		noExt.delete();
		ext.renameTo(noExt);				
	}
	
	@SuppressWarnings("resource")
	public static Teams sliceGetTeamsList(String guid){
		Mat source = imread(IMG_PROC_PATH + guid,CV_LOAD_IMAGE_COLOR);	
		
		Mat scaledSrc = new Mat(defaultScreenshotSize, source.type());
		resize(source, scaledSrc, defaultScreenshotSize);
		Mat sourceGrey = new Mat(scaledSrc.size(), CV_8UC1);
		cvtColor(scaledSrc, sourceGrey, COLOR_BGR2GRAY);
		
		//top left of character area
		Point tl = new Point(400,175);
		Size size = new Size(183, 287);
		Rect theirs = new Rect();
		theirs.y(tl.y());
		theirs.height(size.height());
		theirs.width(size.width());
		Rect ours = new Rect();
		ours.y(tl.y()+size.height());
		ours.height(size.height());
		ours.width(size.width());
		int x=0;
		Teams teams = new Teams();
		for (int i = 0; i < 6; i++) {
			x=i*size.width()+tl.x();
			theirs.x(x);
			ours.x(x);
			CharGuess guessTheirs,guessOurs;
			teams.getTheirs().getCharacters().add(getGuesses(OpenCVUtils.getROI(scaledSrc, theirs)));
			guessTheirs = teams.getTheirs().getCharacters().get(i).getBestGuess();
			teams.getOurs().getCharacters().add(getGuesses(OpenCVUtils.getROI(scaledSrc, ours)));
			guessOurs = teams.getOurs().getCharacters().get(i).getBestGuess();
			log.info("their player" + i + " : " + guessTheirs.getCharacter() + 
						" = " + guessTheirs.getScore().toString());
			log.info("our player" + i + " : " + guessOurs.getCharacter() + 
					" = " + guessOurs.getScore().toString());
		}
		
		return teams;
	}
	
	private static CharacterGuesses getGuesses(Mat roi){
		CharacterGuesses guesses = new CharacterGuesses();
		for (String hero : getCharacters()) {
			Mat template = OpenCVUtils.matFromJar(TEMPLATES_FOLDER + hero + ".png",CV_LOAD_IMAGE_COLOR);
			CharGuess guess = new CharGuess();
			guess.setCharacter(hero);
			guess.setScore(OpenCVUtils.matchScore(roi, template));
			guesses.getCharAndMatchingStrength().add(guess);			
		}		
		return guesses;		
	}
	
	
	
	public static List<String> getCharacters(){
		if (characters != null) {
			return characters;
		} else{
			characters = new ArrayList<String>();
			characters.addAll(java.util.Arrays.asList("ana,bastion,dva,genji,hanzo,junkrat,lucio,mccree,mei,mercy,pharah,reaper,reinhardt,roadhog,soldier,symmetra,torbjorn,tracer,widowmaker,winston,zarya,zenyatta".split(",")));
			return characters;
		}
	}

}
