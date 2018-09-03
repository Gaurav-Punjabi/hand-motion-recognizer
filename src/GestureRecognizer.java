
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author tamanna
 */
public class GestureRecognizer implements GestureConstants{
    public GestureRecognizer(MainFrame ref) {
        this.ref = ref;
        customInit();
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
    private void customInit() {
        this.indexX = indexY = 0;
        this.indexCounter = DELAY_COUNTER;
        try {
            this.robot = new Robot();
        } catch(AWTException awte) {
            System.out.println("AWTException caught : " + awte.getMessage());
        }
    }
    public synchronized void recognizeIndex(Mat image) {
        //Finding the contour
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Mat contourMat = image.clone();
        Imgproc.findContours(contourMat,contours,new Mat(),Imgproc.RETR_EXTERNAL,Imgproc.CHAIN_APPROX_NONE);
        if(contours.size() > 0) {
            indexCounter = DELAY_COUNTER;
            MatOfPoint largestContour = ImageProcessor.getLargestContour(contours);
            Point point = ImageProcessor.getPoint(largestContour);
            if(indexAnchor && !indexGestureRecognized) {
                indexXDisplacement = (int)point.x - indexX;
                indexYDisplacement = (int)point.y - indexY;
                if(indexXDisplacement > indexYDisplacement &&
                   indexXDisplacement >= 130) {
                    indexLeftGesture();
                    this.indexGestureRecognized = true;
                } else if(indexXDisplacement < indexYDisplacement &&
                          indexXDisplacement <= -130) {
                    indexRightGesture();
                    this.indexGestureRecognized = true;
                } else if(indexYDisplacement > indexXDisplacement &&
                          indexYDisplacement >= 130) {
                        indexDownGesture();
                        this.indexGestureRecognized = true;
                } else if(indexYDisplacement < indexXDisplacement &&
                          indexYDisplacement <= -130) {
                        indexUpGesture();
                        this.indexGestureRecognized = true;
                }
            } else if(!indexGestureRecognized) {
                this.indexAnchor = true;
                this.indexGestureRecognized = false;
                this.indexX = (int)point.x;
                this.indexY = (int)point.y;
            }
//            robot.mouseMove((int)point.x, (int)point.y);
        }
        else {
            indexCounter--;   
            if(indexCounter == 0) {
                this.indexAnchor = false;
                this.indexGestureRecognized = false;
            }  
        }
    }
    private void  indexLeftGesture() {
        System.out.println("Left Gesture from index Fingure");
    }
    private void indexDownGesture() {
        System.out.println("Down Gesture from index Finger");
        robot.keyPress(KeyEvent.VK_PAGE_DOWN);
        robot.keyRelease(KeyEvent.VK_PAGE_DOWN);
    }
    private void indexRightGesture() {
        System.out.println("Right Gesture from index Finger");
    }
    private void indexUpGesture() {
        System.out.println("Up Gesture from index Finger");
    }
    private int indexX,indexY;
    private int indexXDisplacement,indexYDisplacement;
    private int indexCounter;
    private boolean indexAnchor = false;
    private boolean indexGestureRecognized = false;
    private MainFrame ref;
    private Robot robot;
}
