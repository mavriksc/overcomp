package com.mavriksc.service;

import static org.bytedeco.javacpp.opencv_core.CV_32FC1;
import static org.bytedeco.javacpp.opencv_core.CV_8UC1;
import static org.bytedeco.javacpp.opencv_core.FONT_HERSHEY_PLAIN;
import static org.bytedeco.javacpp.opencv_highgui.imshow;
import static org.bytedeco.javacpp.opencv_highgui.waitKey;
import static org.bytedeco.javacpp.opencv_imgcodecs.*;
import static org.bytedeco.javacpp.opencv_imgproc.COLOR_BGR2GRAY;
import static org.bytedeco.javacpp.opencv_imgproc.TM_CCORR_NORMED;
import static org.bytedeco.javacpp.opencv_imgproc.cvtColor;
import static org.bytedeco.javacpp.opencv_imgproc.matchTemplate;
import static org.bytedeco.javacpp.opencv_imgproc.putText;
import static org.bytedeco.javacpp.opencv_imgproc.resize;

import java.util.ArrayList;
import java.util.List;

import org.bytedeco.javacpp.indexer.FloatIndexer;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Point;
import org.bytedeco.javacpp.opencv_core.Scalar;
import org.bytedeco.javacpp.opencv_core.Size;


public class MatchingService {	
	
	private static final String TEMPLATES_FOLDER = "ow-templates/";
	private static final String IMG_PROC_PATH = "./img-proc/";
	private static final Size defaultScreenshotSize = new Size(1920, 1080);
	private static List<String> characters;	
	
	
	public static void scaleAndCheckAll(String guid){		
		Mat source = imread(IMG_PROC_PATH + guid);	
		Mat scaledSrc = new Mat(defaultScreenshotSize, source.type());
		resize(source, scaledSrc, defaultScreenshotSize);
		Mat sourceGrey = new Mat(scaledSrc.size(), CV_8UC1);
		cvtColor(scaledSrc, sourceGrey, COLOR_BGR2GRAY);		
		
		for (String hero : getCharacters()) {
			Mat template = OpenCVUtils.readImage(TEMPLATES_FOLDER + hero + ".png", 0);
			Size size = new Size(sourceGrey.cols()-template.cols()+1, sourceGrey.rows()-template.rows()+1);
			Mat result = new Mat(size, CV_32FC1);
			matchTemplate(sourceGrey, template, result, TM_CCORR_NORMED);
			Scalar color =  OpenCVUtils.randColor();
			List<Point> points = getPointsFromMatAboveThreshold(result, 0.90f);
			for (Point point : points) {
				//rectangle(scaledSrc, new Rect(point.x(),point.y(),template.cols(),template.rows()), color, -2, 0, 0);
				putText(scaledSrc, hero, point, FONT_HERSHEY_PLAIN, 2, color);
			}
		}
		imwrite(IMG_PROC_PATH + guid,  source);		
	}
	
	public static List<Point> getPointsFromMatAboveThreshold(Mat m, float t){
		List<Point> matches = new ArrayList<Point>();
		FloatIndexer indexer = m.createIndexer();
		for (int y = 0; y < m.rows(); y++) {
        	for (int x = 0; x < m.cols(); x++) {
        		if (indexer.get(y,x)>t) {
					System.out.println("(" + x + "," + y +") = "+ indexer.get(y,x));
					matches.add(new Point(x, y));					
				}
			}			
		}		
		return matches;
	}	
	
	private static List<String> getCharacters(){
		if (characters != null) {
			return characters;
		} else{
			characters = new ArrayList<String>();
			characters.addAll(java.util.Arrays.asList("genji,mccree,pharah,reaper,soldier,tracer,widowmaker,torbjorn,mei,junkrat,hanzo,bastion,dva,reinhardt,roadhog,winston,zarya,zenyatta,symmetra,mercy,lucio,ana".split(",")));
			return characters;
		}
	}

}
