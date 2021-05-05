/**
 * 
 */
package br.com.codiub.heimdall.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import com.lowagie.text.pdf.codec.Base64.OutputStream;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.Thumbnails.Builder;
import net.coobird.thumbnailator.geometry.Positions;

/**
 * @author adamis.rocha
 *
 */
public class ResizeImages {
	
	
	BufferedImage originalImage;
	Builder<BufferedImage> of;
	
	public ResizeImages(File file) throws IOException {
		originalImage = ImageIO.read(file);
		of = Thumbnails.of(originalImage);
		of.imageType(BufferedImage.TYPE_INT_ARGB);		
	}

	/**
	 * Rotaciona a imagem em @param angulo
	 * @param angulo
	 */
	public void setRotate(double angulo) {
		of.rotate(angulo);
	}
	
	/**
	 * Define a qualidade
	 * @param qualit
	 */
	public void setQualit(double qualit) {		
		qualit = qualit/100.0;
		
		of.outputQuality(qualit);
	}
	
	public void setScale(Integer percent) {
		of.scale((double)percent/100.0);
	}
	
	public void setForceScale(Integer width, Integer height) {
		of.forceSize(width, height);
	}
	
	public void setWidth(Integer width) {
		//of.width(width);//,8000);
		of.size(width,8000);
	}
	
	public void setHeight(Integer height) {
		
		of.size(8000,height);
		//of.height(height);
	}
	
	public void setWaterMaker(File watermark,Positions positions) throws IOException {
		BufferedImage watermarkImage = ImageIO.read(watermark);
		of.watermark(positions, watermarkImage, 0.5f);
	}	
	
	public BufferedImage getBufferImage() throws IOException {
		return of.asBufferedImage();
	}
	
	public void getFile(File file) throws IOException {		
		of.toFile(file);
	}

	public void getOutputStream(OutputStream os) throws IOException {
		of.outputFormat("png");
		of.toOutputStream(os);
	}

	
}
