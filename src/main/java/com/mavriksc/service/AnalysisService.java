package com.mavriksc.service;

import static org.bytedeco.javacpp.opencv_core.CV_32FC1;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Size;

import com.mavriksc.types.TeamsConfirmed;
import com.mavriksc.types.ValPos;

import org.bytedeco.javacpp.indexer.FloatIndexer;

public class AnalysisService {
	
	private static final String DATA_FILE = "data/strengthmap.csv";
	private Mat compMatrix;
	FloatIndexer compIndexer;
	private List<String> characters;
	private int size;

	public AnalysisService() {
		super();
		init();
		characters = MatchingService.getCharacters();
	}
	
	public String yourSwitch(TeamsConfirmed teams){
		List<Float> cSum = crossSum(teams);
		List<Integer>ourTeamIndexes = new ArrayList<>();
		List<Integer> tsix = topSix(cSum);
		for (Integer i : tsix) {
			System.out.println(characters.get(i));
		}
		for (String character : teams.getOurs()) {
			ourTeamIndexes.add(characters.indexOf(character));
		}
		return "";
	}
	
	public List<String> bestSwitch(TeamsConfirmed teams){
		return null;
	}
	
	public List<String> bestTeam(TeamsConfirmed teams){
		return null;
	}
	
	
	private List<Float> crossSum(TeamsConfirmed teams){
		@SuppressWarnings("resource")
		Mat subMat = new Mat(new Size(6,size),CV_32FC1);
		FloatIndexer fi = subMat.createIndexer();
		
		for (int x = 0; x < teams.getTheirs().size(); x++) {
			int charIndex = characters.indexOf(teams.getTheirs().get(x));
			for (int y = 0; y < subMat.rows(); y++) {
				fi.put(y, x,compIndexer.get(y,charIndex));				
			}
		}
		List<Float> cSum = new ArrayList<Float>();
		for (int y = 0; y < subMat.rows(); y++) {
			float f=0f;
			for (int x = 0; x < subMat.cols(); x++) {
				f+=fi.get(y,x);
			}
			cSum.add(f);
		}		
		return cSum;
	}
	
	private List<Integer> topSix(List<Float> f){
		List<Integer> topS= new ArrayList<>();
		List<ValPos> list = new ArrayList<>();
		for (int i = 0; i < f.size(); i++) {
			list.add(new ValPos(f.get(i), i));
		}
		list.sort(null);
		for (int i = 0; i < 6; i++) {
			topS.add(list.get(i).getPos());
		}
		
		return topS;
	}
	
	

	private void init() {
		InputStreamReader in = new InputStreamReader(getClass()
													.getClassLoader()
													.getResourceAsStream(DATA_FILE));
		try {
			List<CSVRecord> records = CSVFormat.RFC4180.parse(in).getRecords();
			size = records.size()-1;
			compMatrix = new Mat(new Size(size,size),CV_32FC1);
			FloatIndexer indexer = compMatrix.createIndexer();
			for (int y = 1; y < size+1; y++) {
				CSVRecord r = records.get(y);
				for (int x = 1; x < size+1; x++) {
					indexer.put(y-1, x-1, Float.valueOf(r.get(x)));					
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		compIndexer = compMatrix.createIndexer();		
	}
	
}
