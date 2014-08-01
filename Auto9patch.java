import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;


public class Auto9patch {

	public static void main (String arg[]){
		if(arg.length < 1){
			System.err.println("No Image Parameter");
			System.exit(0);
		}
		Image image = null;
		try {
		    URL url = new File(arg[0]).toURI().toURL();
		    image = Toolkit.getDefaultToolkit().createImage(url);
		} catch (Exception e){
			System.err.println("Error in retrieving File");
			System.err.println(e);
		}
		BufferedImage i = toBufferedImage(image);
		int w = i.getWidth();	
		int h = i.getHeight();
		System.out.println("Original image: " + w + "x" + h);
		
		//setup new array for modified image
		int[][] modified_array = new int[w+2][h+2];
		int[][] array = new int[w][h];
		for (int j = 0; j < w; j++) {
		    for (int k = 0; k < h; k++) {
		        array[j][k] = i.getRGB(j, k); // or whatever
		        modified_array[j+1][k+1] = array[j][k];
		     }
		}
		
		
		//null image space counts as black by default
		//top & bottom
		for(int j = 1; j < w; j++){
			if(!Arrays.equals(modified_array[j],modified_array[j+1])){
				modified_array[j][0] = new Color(255, 255,255).getRGB();
				modified_array[j][h+1] = new Color(255, 255,255).getRGB();
			}
		}
		
		//left and right
		int[] temp_array1 = new int[h];
		int[] temp_array2 = new int[h];
		
		for(int l = 0; l != array.length;l++){
			temp_array2[l] = array[l][0];
		}	
		for(int l = 0; l != array.length;l++){
			temp_array1[l] = array[l][1];
		}
		for(int k = 1; k < h; k++){
			if(!Arrays.equals(temp_array1,temp_array2)){
				modified_array[0][k] = new Color(255,255,255).getRGB();
				modified_array[w+1][k] = new Color(255,255,255).getRGB();
			}
			System.arraycopy(temp_array1, 0, temp_array2, 0, temp_array1.length);
			for(int l = 0; l != array.length;l++){
				temp_array1[l] = array[l][k];
			}	
		}

		//corners must be white
		modified_array[0][0] = new Color(255, 255,255).getRGB();
		modified_array[0][h+1] = new Color(255, 255,255).getRGB();
		modified_array[w+1][0] = new Color(255, 255,255).getRGB();
		modified_array[w+1][h+1] = new Color(255, 255,255).getRGB();
		
		
//		Conversion to PNG
		BufferedImage theImage = new BufferedImage(w+2, h+2, BufferedImage.TYPE_INT_RGB);
	    for(int x = 0; x<modified_array.length; x++){
	        for(int y = 0; y<modified_array[x].length; y++){
	            theImage.setRGB(x, y, modified_array[x][y]);
	        }
	    }
	    File outputfile = new File("modified.9.png");
	    try {
			ImageIO.write(theImage, "png", outputfile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Complete");
	}	
	
	public static BufferedImage toBufferedImage(Image image) {
	    if (image instanceof BufferedImage) {
	        // Return image unchanged if it is already a BufferedImage.
	        return (BufferedImage) image;
	    }
	
	    // Ensure image is loaded.
	    image = new ImageIcon(image).getImage();

	    BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null),  BufferedImage.TYPE_INT_ARGB);
	    Graphics g = bufferedImage.createGraphics();
	    g.drawImage(image, 0, 0, null);
	    g.dispose();

    	return bufferedImage;
	}
}
