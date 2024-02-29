package demo;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBufferByte;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.util.Hashtable;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.EyesRunner;
import com.applitools.eyes.RectangleSize;
import com.applitools.eyes.TestResults;
import com.applitools.eyes.TestResultsSummary;
import com.applitools.eyes.config.Configuration;
import com.applitools.eyes.images.Eyes;
import com.applitools.eyes.images.ImageRunner;
import com.applitools.eyes.images.Target;
import com.hp.lft.sdk.Aut;
import com.hp.lft.sdk.Desktop;
import com.hp.lft.sdk.GeneralLeanFtException;
import com.hp.lft.sdk.HorizontalVisualRelation;
import com.hp.lft.sdk.VerticalVisualRelation;
import com.hp.lft.sdk.VisualRelation;
import com.hp.lft.sdk.stdwin.Button;
import com.hp.lft.sdk.stdwin.ButtonDescription;
import com.hp.lft.sdk.stdwin.Static;
import com.hp.lft.sdk.stdwin.StaticDescription;
import com.hp.lft.sdk.stdwin.Window;
import com.hp.lft.sdk.stdwin.WindowDescription;
import com.hp.lft.verifications.Verify;

public class UftDemo {
	
    private static EyesRunner runner = new ImageRunner();
    private static Configuration config = new Configuration();
    private static BatchInfo batch = new BatchInfo("UFT Tests");
    ThreadLocal<Eyes> threadedEyes = new ThreadLocal<Eyes>();
    
    @BeforeAll
    public static void setUp(){        
        //config.setServerUrl("https://eyes.applitools.com/"); //set by default
        // Define the OS and hosting application to identify the baseline.
        config.setHostOS("Windows 11");
        config.setHostApp("My App");
        batch.setNotifyOnCompletion(true);
        config.setBatch(batch);
    }
    
    @BeforeEach
    public void startTest(TestInfo testInfo) {
    	Eyes eyes = new Eyes(runner);
        eyes.setConfiguration(config);
        eyes.open("Demo App - Images Java", testInfo.getDisplayName(), new RectangleSize(800, 600));
        threadedEyes.set(eyes);
    }
    
    public BufferedImage convertRenderedImage(RenderedImage img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage)img;  
        }   
        ColorModel cm = img.getColorModel();
        int width = img.getWidth();
        int height = img.getHeight();
        WritableRaster raster = cm.createCompatibleWritableRaster(width, height);
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
    	Hashtable<String,Object> properties = new Hashtable<String,Object>();
        String[] keys = img.getPropertyNames();
        if (keys!=null) {
            for (int i = 0; i < keys.length; i++) {
                properties.put(keys[i], img.getProperty(keys[i]));
            }
        }
        BufferedImage result = new BufferedImage(cm, raster, isAlphaPremultiplied, properties);
        img.copyData(raster);
        return result;
    }
    
    @Test
    public void testAutFullPath() throws GeneralLeanFtException {
    	Eyes eyes = threadedEyes.get();
    	Aut calc = Desktop.launchAut("C:\\Windows\\System32\\calc.exe");
    	//your test code goes here
    	try {
    		//Create the WindowDescription object for the Calculator application window.
    		WindowDescription calculatorDescription = new WindowDescription.Builder().nativeClass("CalcFrame").build();
    			 
    		// Locate the Calculator window and assign it to an Window object.
    		Window calculator = Desktop.describe(Window.class, calculatorDescription);
    		
    		DataBufferByte screenshotBuffer = (DataBufferByte) calculator.getSnapshot().getData().getDataBuffer();
    		eyes.check(Target.image(screenshotBuffer.getData()).withName("Main Window"));
    			
    		// Identify the Calculator window static text.
    		Static textBox = calculator.describe(Static.class, new StaticDescription.Builder().windowId(150).nativeClass("Static").build());
    		screenshotBuffer = (DataBufferByte) textBox.getSnapshot().getData().getDataBuffer();
    		eyes.check(Target.image(screenshotBuffer.getData()).withName("Textbox"));
    
    		
    	} finally {
    		// Exit and close the Calculator.
    		calc.close();
    	}
    	threadedEyes.set(eyes);
    }
    
    @Test
    public void testCalcButtonsUsingVri() throws GeneralLeanFtException, IOException, InterruptedException {
    	Eyes eyes = threadedEyes.get();
    	
    	//Launch the Calculator application.
    	Process calc = new ProcessBuilder("C:\\Windows\\System32\\calc.exe").start();
    		
    	//Pause to ensure Calculator has fully opened. 
    	Thread.sleep(4 * 1000); 
            
    	try {
    		//Create the WindowDescription object for the Calculator application window.
    		WindowDescription calculatorDescription = new WindowDescription.Builder().nativeClass("CalcFrame").build();
    			 
    		// Locate the Calculator window and assign it to an Window object.
    		Window calculator = Desktop.describe(Window.class, calculatorDescription);
    		
    		//BufferedImage screenshot = convertRenderedImage(calculator.getSnapshot());
    		//eyes.check(Target.image(screenshot).withName("Main Window"));
    		
    		DataBufferByte screenshotBuffer = (DataBufferByte) calculator.getSnapshot().getData().getDataBuffer();
    		eyes.check(Target.image(screenshotBuffer.getData()).withName("Main Window"));
    			
    		// Identify the Calculator window static text.
    		Static textBox = calculator.describe(Static.class, new StaticDescription.Builder().windowId(150).nativeClass("Static").build());
    
    		// Locate the number 2 and number 4 buttons on the Calculator.
    		Button button4 = calculator.describe(Button.class, new ButtonDescription.Builder().windowId(134).nativeClass("Button").build());
    		Button button2 = calculator.describe(Button.class, new ButtonDescription.Builder().windowId(132).nativeClass("Button").build());
    
    		// Locate the number 1 button on the Calculator based on the location of the number 2 and 4 buttons.
    		Button button1 = calculator.describe(Button.class, new ButtonDescription.Builder().nativeClass("Button").
    		vri(
    			// The number 2 button is to the right of the number 1 button.
    			new VisualRelation().setTestObject(button2).setHorizontalRelation(HorizontalVisualRelation.RIGHT),
    			// The number 4 button is above the number 1 button. 
    			new VisualRelation().setTestObject(button4).setVerticalRelation(VerticalVisualRelation.ABOVE)).build());
    			
    		screenshotBuffer = (DataBufferByte) button1.getSnapshot().getData().getDataBuffer();
    		eyes.check(Target.image(screenshotBuffer.getData()).withName("1  Button"));
    		
    		// Click the number 1 button. 
    		button1.click();
    		// The number 1 should appear on the Calculator's output screen. 
    		String visibleText = textBox.getVisibleText();
    		// Ensure that the number 1 appears on the screen. 
    		Verify.areEqual("1", visibleText, "TestCalcButtonsUsingVri", "Verify that the number '1' appears on the calculator's output screen.");
    	} finally {
    		// Exit and close the Calculator.
    		calc.destroy();
    	}
    	threadedEyes.set(eyes);
    }
    
    @AfterEach
    public void endTest() {
    	Eyes eyes = threadedEyes.get();
    	if(eyes != null && eyes.getIsOpen()) {
    		TestResults testResults = eyes.close(false);
    		System.out.println(testResults.toString());
    	}
    }
    
    @AfterAll
    public void tearDown() {
    	if(runner != null) {
    		TestResultsSummary results = runner.getAllTestResults();
    		System.out.println(results.toString());
    	}
    }

}
