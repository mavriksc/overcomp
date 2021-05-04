package com.mavriksc.service;

//http://answers.opencv.org/question/10236/opencv-java-api-highguiimread-and-paths-pointing-to-files-in-a-jar/
//credit to Lucky Luke 
//i made some updates but i'm not sure about the JavaIO part 
//  as nothing i've loaded used that code yet
import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Point;
import org.bytedeco.javacpp.opencv_core.Rect;
import org.bytedeco.javacpp.opencv_core.Scalar;
import org.bytedeco.javacpp.opencv_core.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.indexer.ByteIndexer;
import org.bytedeco.javacpp.indexer.FloatIndexer;

import static org.bytedeco.javacpp.opencv_imgcodecs.*;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.imageio.ImageIO;

public class OpenCVUtils {
	private static final Logger log = LoggerFactory.getLogger(OpenCVUtils.class);
	private OpenCVUtils() {}
	 /**
    * 8bit, 3-channel image.
    * @see Highgui.CV_LOAD_IMAGE_COLOR
    */
    public static final int LOAD_COLOR = 3;

    /**
    * 8bit, 1-channel image.
    * @see Highgui.CV_LOAD_IMAGE_GRAYSCALE
    */
    public static final int LOAD_GRAYSCALE = 1;
	
	public static Mat readImage(String name, int flags) {
		URL url = ClassLoader.getSystemResource(name);

        // make sure the file exists
        if (url == null) {
            System.out.println("ResourceNotFound: " + name);
            return new Mat();
        }

        String path = url.getPath();

        // not sure why we (sometimes; while running unpacked from the IDE) end 
        // up with the authority-part of the path (a single slash) as prefix,
        // ...anyways: Highgui.imread can't handle it, so that's why.
        if (path.startsWith("/")) {
            path = path.substring(1);
        }

        Mat image = imread(path,flags);
        if (image.empty()) {
        	System.out.println("normal loading failed WHYYY???");
            BufferedImage buf;

            try {
                buf = ImageIO.read(url);
            } catch (IOException e) {
                System.out.println("IOException: " + e.getMessage());
                return image;
            }

            int height = buf.getHeight();
            int width = buf.getWidth();
            int rgb, type, channels;

            switch (flags) {
                case LOAD_GRAYSCALE:
                    type = CV_LOAD_IMAGE_GRAYSCALE;
                    channels = 1;
                    break;
                case LOAD_COLOR:
                default:
                    type = CV_LOAD_IMAGE_COLOR;
                    channels = 3;
                    break;
            }

            byte[] px = new byte[channels];
            image = new Mat(height, width, type);
            ByteIndexer bi = image.createIndexer();
            

            for (int y=0; y<height; y++) {
                for (int x=0; x<width; x++) {
                    rgb = buf.getRGB(x, y);
                    px[0] = (byte)(rgb & 0xFF);
                    if (channels==3) {
                        px[1] = (byte)((rgb >> 8) & 0xFF);
                        px[2] = (byte)((rgb >> 16) & 0xFF);
                    }
                    bi.put(y, x, px);
                    
                }
            }            
        }

        return image;
		
	}
	
	public static Mat readImage(String name) {
        return readImage(name, LOAD_COLOR);
    }

	public static Mat getROICopy(Mat m,Rect r){		
		Mat roi = new Mat(r.size(),m.type());
		m.rowRange(r.y(),r.y()+r.height()).colRange(r.x(), r.x()+r.width()).copyTo(roi);
		return roi;
		
	}
	 
	public static Mat getROI(Mat m,Rect r){		
		return m.rowRange(r.y(),r.y()+r.height()).colRange(r.x(), r.x()+r.width());
			
	}
	
	public static Scalar randColor(){
		int b,g,r;
		b= ThreadLocalRandom.current().nextInt(0, 255 + 1);
		g= ThreadLocalRandom.current().nextInt(0, 255 + 1);
		r= ThreadLocalRandom.current().nextInt(0, 255 + 1);
		return new Scalar (b,g,r,0);
	}
	
	public static Mat matFromJar(String path, int flags) {
		InputStream is = OpenCVUtils.class.getClassLoader().getResourceAsStream(path);
		if (is == null) {
            System.out.println("ResourceNotFound: " + path);
            return new Mat();
        }
		int nRead;
		byte[] data = new byte[16 * 1024];
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		try {
			while ((nRead = is.read(data, 0, data.length)) != -1) {
			    buffer.write(data, 0, nRead);
			}
		} catch (IOException e) {
			log.error("something happened",e);
		}
		byte[] bytes = buffer.toByteArray();
		Mat mat = imdecode(new Mat(bytes), flags);
		return mat;
	}
	
	public static Mat matFromJar(String path){
		return matFromJar(path,CV_LOAD_IMAGE_UNCHANGED);
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
	
	public static float matchScore(Mat src,Mat tmp){
		
		Size size = new Size(src.cols()-tmp.cols()+1, src.rows()-tmp.rows()+1);
		Mat result = new Mat(size, CV_32FC1);
		matchTemplate(src, tmp, result, TM_CCORR_NORMED);
		DoublePointer minVal= new DoublePointer();
		DoublePointer maxVal= new DoublePointer();
		Point min = new Point();
		Point max = new Point();
		FloatIndexer fi = result.createIndexer();
		minMaxLoc(result, minVal, maxVal, min, max, null);		
		return fi.get(max.y(),max.x());
	} 


}
