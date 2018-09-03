
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
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
public class ImageProcessor {
        static {
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        }
        public static BufferedImage toBufferedImage(Mat mat) {
            int type = BufferedImage.TYPE_BYTE_GRAY;
            if(mat.channels() > 1) {
                type = BufferedImage.TYPE_3BYTE_BGR;
            }
            byte[] buffer = new byte[(int)(mat.total() * mat.elemSize())];
            mat.get(0, 0, buffer);
            BufferedImage image = new BufferedImage(mat.cols(), mat.rows(), type);
            final byte[] targetPixels = ((DataBufferByte)image.getRaster().getDataBuffer()).getData();
            System.arraycopy(buffer, 0, targetPixels, 0, (int)(mat.total() * mat.elemSize()));
            return image;
        }
        public static MatOfPoint getLargestContour(List<MatOfPoint> contours) {
            if(contours == null) {
                return null;
            }
            if(contours.size() < 0) {
                return null;
            }
            MatOfPoint largestContour = contours.get(0);
            for(MatOfPoint currentContour : contours) {
                if(Imgproc.contourArea(currentContour) > Imgproc.contourArea(largestContour)) {
                    largestContour = currentContour;
                }
            }
            return largestContour;
        }
        public static Point getPoint(MatOfPoint contour) {
            if(contour == null) {
                return null;
            }
            return contour.toList().get(0);
        }
}
