/*
 *    JImageIO-extension - OpenSource Java Image translation Library
 *    http://www.geo-solutions.it/
 *	  https://imageio-ext.dev.java.net/
 *    (C) 2007, GeoSolutions
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package it.geosolutions.imageio.plugins.jp2kakadu;

import it.geosolutions.imageio.gdalframework.Viewer;
import it.geosolutions.imageio.stream.output.FileImageOutputStreamExtImpl;
import it.geosolutions.resources.TestData;

import java.awt.Rectangle;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;

import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.media.jai.JAI;
import javax.media.jai.ParameterBlockJAI;
import javax.media.jai.RenderedOp;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.sun.media.jai.operator.ImageWriteDescriptor;

/**
 * Class for testing all supported Kakadu Create Options
 * 
 * @author Daniele Romagnoli, GeoSolutions.
 * @author Simone Giannecchini, GeoSolutions.
 */
public class JP2KWriteTest extends AbstractJP2KTestCase {

	/** The LOGGER for this class. */
	private static final Logger LOGGER = Logger
			.getLogger("it.geosolutions.imageio.plugins.jp2kakadu");

	final private static String HOLD_WRITTEN = "it.geosolutions.imageio.plugins.jp2kakadu.holdwrittenfiles";

	final static boolean deleteTempFilesOnExit = !Boolean
			.getBoolean(HOLD_WRITTEN);
	final static String testFileName = "test.jp2";

	// When testing write operations on very big images, performing subsampled
	// read may be useful.
	final static boolean ENABLE_SUBSAMPLING = false;

	// final static String testFileName = "spezia_wgs84.tiff";

	public JP2KWriteTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Testing "Clevels" Create Option.
	//
	// ////////////////////////////////////////////////////////////////////////
	public void testWrite_Clevels() throws IOException, FileNotFoundException {

		LOGGER.info("Testing JP2 Write operation with Clevels option setting");
		// //
		// Preparing input/output files
		// //
		final File inputFile = TestData.file(this, testFileName);
		assertTrue(inputFile.exists());

		// Output files resulting from 2 different values of the same create
		// option. In this test, the create option is Clevels.

		final int firstClevelsParam = 2;
		final int secondClevelsParam = 6;
		final String fileName1 = new StringBuffer("Clevels-").append(
				Integer.toString(firstClevelsParam)).append("-.jp2").toString();
		final String fileName2 = new StringBuffer("Clevels-").append(
				Integer.toString(secondClevelsParam)).append("-.jp2")
				.toString();
		final File outputFile1 = TestData.temp(this, fileName1,
				deleteTempFilesOnExit);
		final File outputFile2 = TestData.temp(this, fileName2,
				deleteTempFilesOnExit);

		// //
		// Preparing to read
		// //
		final ParameterBlockJAI pbjImageRead = new ParameterBlockJAI(
				"ImageRead");
		pbjImageRead.setParameter("Input", inputFile);
		if (ENABLE_SUBSAMPLING) {
			ImageReadParam readParam = new ImageReadParam();
			readParam.setSourceSubsampling(4, 4, 0, 0);
			pbjImageRead.setParameter("readParam", readParam);
		}

		// Reading
		pbjImageRead.setParameter("Reader", new JP2GDALKakaduImageReaderSpi()
				.createReaderInstance());
		RenderedOp image = JAI.create("ImageRead", pbjImageRead);

		// ////////////////////////////////////////////////////////////////////
		//
		// preparing to write (1ST version of the create option test)
		//
		// ////////////////////////////////////////////////////////////////////
		// Setting output and writer
		final ParameterBlockJAI pbjImageWrite = new ParameterBlockJAI(
				"ImageWrite");
		pbjImageWrite.setParameter("Output", new FileImageOutputStreamExtImpl(
				outputFile1));
		ImageWriter writer = new JP2GDALKakaduImageWriterSpi()
				.createWriterInstance();
		pbjImageWrite.setParameter("Writer", writer);

		// Specifying image source to write
		pbjImageWrite.addSource(image);
		ImageWriteParam param = writer.getDefaultWriteParam();

		// Specifying the required create option
		((JP2GDALKakaduImageWriteParam) param).setClevels(firstClevelsParam);
		pbjImageWrite.setParameter("writeParam", param);

		// Writing
		final RenderedOp op = JAI.create("ImageWrite", pbjImageWrite);

		// ////////////////////////////////////////////////////////////////////
		//
		// preparing to write (2ND version of the create option test)
		//
		// ////////////////////////////////////////////////////////////////////
		// Setting output and writer
		final ParameterBlockJAI pbjImageWrite2 = new ParameterBlockJAI(
				"ImageWrite");
		pbjImageWrite2.setParameter("Output", new FileImageOutputStreamExtImpl(
				outputFile2));
		ImageWriter writer2 = new JP2GDALKakaduImageWriterSpi()
				.createWriterInstance();
		pbjImageWrite2.setParameter("Writer", writer2);

		// Specifying image source to write
		pbjImageWrite2.addSource(image);
		ImageWriteParam param2 = writer2.getDefaultWriteParam();

		// Specifying the required create option
		((JP2GDALKakaduImageWriteParam) param2).setClevels(secondClevelsParam);
		pbjImageWrite2.setParameter("writeParam", param2);

		// Writing
		final RenderedOp op2 = JAI.create("ImageWrite", pbjImageWrite2);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Testing "Clayers" Create Option.
	//
	// ////////////////////////////////////////////////////////////////////////
	public void testWrite_Clayers() throws IOException, FileNotFoundException {

		LOGGER.info("Testing JP2 Write operation with Clayers option setting");
		// //
		// Preparing input/output files
		// //
		final File inputFile = TestData.file(this, testFileName);
		assertTrue(inputFile.exists());

		// Output files resulting from 2 different values of the same create
		// option. In this test, the create option is Clayers.

		final int firstClayersParam = 3;
		final int secondClayersParam = 20;
		final String fileName1 = new StringBuffer("Clayers-").append(
				Integer.toString(firstClayersParam)).append("-.jp2").toString();
		final String fileName2 = new StringBuffer("Clayers-").append(
				Integer.toString(secondClayersParam)).append("-.jp2")
				.toString();
		final File outputFile1 = TestData.temp(this, fileName1,
				deleteTempFilesOnExit);
		final File outputFile2 = TestData.temp(this, fileName2,
				deleteTempFilesOnExit);

		// //
		// Preparing to read
		// //
		final ParameterBlockJAI pbjImageRead = new ParameterBlockJAI(
				"ImageRead");
		pbjImageRead.setParameter("Input", inputFile);
		if (ENABLE_SUBSAMPLING) {
			ImageReadParam readParam = new ImageReadParam();
			readParam.setSourceSubsampling(4, 4, 0, 0);
			pbjImageRead.setParameter("readParam", readParam);
		}
		// Reading
		pbjImageRead.setParameter("Reader", new JP2GDALKakaduImageReaderSpi()
				.createReaderInstance());
		RenderedOp image = JAI.create("ImageRead", pbjImageRead);

		// ////////////////////////////////////////////////////////////////////
		//
		// preparing to write (1ST version of the create option test)
		//
		// ////////////////////////////////////////////////////////////////////
		// Setting output and writer
		final ParameterBlockJAI pbjImageWrite = new ParameterBlockJAI(
				"ImageWrite");
		pbjImageWrite.setParameter("Output", new FileImageOutputStreamExtImpl(
				outputFile1));
		ImageWriter writer = new JP2GDALKakaduImageWriterSpi()
				.createWriterInstance();
		pbjImageWrite.setParameter("Writer", writer);

		// Specifying image source to write
		pbjImageWrite.addSource(image);
		ImageWriteParam param = writer.getDefaultWriteParam();

		// Specifying the required create option
		((JP2GDALKakaduImageWriteParam) param).setClayers(firstClayersParam);
		pbjImageWrite.setParameter("writeParam", param);

		// Writing
		final RenderedOp op = JAI.create("ImageWrite", pbjImageWrite);

		// ////////////////////////////////////////////////////////////////////
		//
		// preparing to write (2ND version of the create option test)
		//
		// ////////////////////////////////////////////////////////////////////
		// Setting output and writer
		final ParameterBlockJAI pbjImageWrite2 = new ParameterBlockJAI(
				"ImageWrite");
		pbjImageWrite2.setParameter("Output", new FileImageOutputStreamExtImpl(
				outputFile2));
		ImageWriter writer2 = new JP2GDALKakaduImageWriterSpi()
				.createWriterInstance();
		pbjImageWrite2.setParameter("Writer", writer2);

		// Specifying image source to write
		pbjImageWrite2.addSource(image);
		ImageWriteParam param2 = writer2.getDefaultWriteParam();

		// Specifying the required create option
		((JP2GDALKakaduImageWriteParam) param2).setClayers(secondClayersParam);
		pbjImageWrite2.setParameter("writeParam", param2);

		// Writing
		final RenderedOp op2 = JAI.create("ImageWrite", pbjImageWrite2);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Testing "Cprecincts" Create Option.
	//
	// ////////////////////////////////////////////////////////////////////////
	public void testWrite_Cprecincts() throws IOException,
			FileNotFoundException {

		LOGGER
				.info("Testing JP2 Write operation with Cprecincts option setting");
		// //
		// Preparing input/output files
		// //
		final File inputFile = TestData.file(this, testFileName);
		assertTrue(inputFile.exists());

		// Output files resulting from 2 different values of the same create
		// option. In this test, the create option is Cprecincts.
		final File outputFile1 = TestData.temp(this, "CprecintsA-.jp2",
				deleteTempFilesOnExit);
		final File outputFile2 = TestData.temp(this, "CprecintsB-.jp2",
				deleteTempFilesOnExit);

		// //
		// Preparing to read
		// //
		final ParameterBlockJAI pbjImageRead = new ParameterBlockJAI(
				"ImageRead");
		pbjImageRead.setParameter("Input", inputFile);
		if (ENABLE_SUBSAMPLING) {
			ImageReadParam readParam = new ImageReadParam();
			readParam.setSourceSubsampling(4, 4, 0, 0);
			pbjImageRead.setParameter("readParam", readParam);
		}
		pbjImageRead.setParameter("Reader", new JP2GDALKakaduImageReaderSpi()
				.createReaderInstance());
		RenderedOp image = JAI.create("ImageRead", pbjImageRead);

		// ////////////////////////////////////////////////////////////////////
		//
		// preparing to write (1ST version of the create option test)
		//
		// ////////////////////////////////////////////////////////////////////
		// Setting output and writer
		final ParameterBlockJAI pbjImageWrite = new ParameterBlockJAI(
				"ImageWrite");
		pbjImageWrite.setParameter("Output", new FileImageOutputStreamExtImpl(
				outputFile1));
		ImageWriter writer = new JP2GDALKakaduImageWriterSpi()
				.createWriterInstance();
		pbjImageWrite.setParameter("Writer", writer);

		// Specifying image source to write
		pbjImageWrite.addSource(image);
		ImageWriteParam param = writer.getDefaultWriteParam();

		// Specifying the required create option
		((JP2GDALKakaduImageWriteParam) param)
				.setCprecincts("{256,256},{256,256},{128,128}");
		pbjImageWrite.setParameter("writeParam", param);

		// Writing
		final RenderedOp op = JAI.create("ImageWrite", pbjImageWrite);

		// ////////////////////////////////////////////////////////////////////
		//
		// preparing to write (2ND version of the create option test)
		//
		// ////////////////////////////////////////////////////////////////////
		// Setting output and writer
		final ParameterBlockJAI pbjImageWrite2 = new ParameterBlockJAI(
				"ImageWrite");
		pbjImageWrite2.setParameter("Output", new FileImageOutputStreamExtImpl(
				outputFile2));
		ImageWriter writer2 = new JP2GDALKakaduImageWriterSpi()
				.createWriterInstance();
		pbjImageWrite2.setParameter("Writer", writer2);

		// Specifying image source to write
		pbjImageWrite2.addSource(image);
		ImageWriteParam param2 = writer2.getDefaultWriteParam();

		// Specifying the required create option
		((JP2GDALKakaduImageWriteParam) param2)
				.setCprecincts("{512,512},{256,512},{128,512},{64,512},{32,512},{16,512},{8,512},{4,512},{2,512}");
		pbjImageWrite2.setParameter("writeParam", param2);

		// Writing
		final RenderedOp op2 = JAI.create("ImageWrite", pbjImageWrite2);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Testing "Corder" Create Option.
	//
	// ////////////////////////////////////////////////////////////////////////
	public void testWrite_Corder() throws IOException, FileNotFoundException {

		LOGGER.info("Testing JP2 Write operation with Corder option setting");
		// //
		// Preparing input/output files
		// //
		final File inputFile = TestData.file(this, testFileName);
		assertTrue(inputFile.exists());

		// Output files resulting from 2 different values of the same create
		// option. In this test, the create option is Corder.
		final File outputFile1 = TestData.temp(this, "CorderPCRL-.jp2",
				deleteTempFilesOnExit);
		final File outputFile2 = TestData.temp(this, "CorderRPCL-.jp2",
				deleteTempFilesOnExit);
		final File outputFile3 = TestData.temp(this, "CorderLRCP-.jp2",
				deleteTempFilesOnExit);

		// //
		// Preparing to read
		// //
		final ParameterBlockJAI pbjImageRead = new ParameterBlockJAI(
				"ImageRead");
		pbjImageRead.setParameter("Input", inputFile);
		if (ENABLE_SUBSAMPLING) {
			ImageReadParam readParam = new ImageReadParam();
			readParam.setSourceSubsampling(4, 4, 0, 0);
			pbjImageRead.setParameter("readParam", readParam);
		}
		pbjImageRead.setParameter("Reader", new JP2GDALKakaduImageReaderSpi()
				.createReaderInstance());
		RenderedOp image = JAI.create("ImageRead", pbjImageRead);

		// ////////////////////////////////////////////////////////////////////
		//
		// preparing to write (1ST version of the create option test)
		//
		// ////////////////////////////////////////////////////////////////////
		// Setting output and writer
		final ParameterBlockJAI pbjImageWrite = new ParameterBlockJAI(
				"ImageWrite");
		pbjImageWrite.setParameter("Output", new FileImageOutputStreamExtImpl(
				outputFile1));
		ImageWriter writer = new JP2GDALKakaduImageWriterSpi()
				.createWriterInstance();
		pbjImageWrite.setParameter("Writer", writer);

		// Specifying image source to write
		pbjImageWrite.addSource(image);
		ImageWriteParam param = writer.getDefaultWriteParam();

		// Specifying the required create option
		((JP2GDALKakaduImageWriteParam) param).setCorder("PCRL");
		pbjImageWrite.setParameter("writeParam", param);

		// Writing
		final RenderedOp op = JAI.create("ImageWrite", pbjImageWrite);

		// ////////////////////////////////////////////////////////////////////
		//
		// preparing to write (2ND version of the create option test)
		//
		// ////////////////////////////////////////////////////////////////////
		// Setting output and writer
		final ParameterBlockJAI pbjImageWrite2 = new ParameterBlockJAI(
				"ImageWrite");
		pbjImageWrite2.setParameter("Output", new FileImageOutputStreamExtImpl(
				outputFile2));
		ImageWriter writer2 = new JP2GDALKakaduImageWriterSpi()
				.createWriterInstance();
		pbjImageWrite2.setParameter("Writer", writer2);

		// Specifying image source to write
		pbjImageWrite2.addSource(image);
		ImageWriteParam param2 = writer2.getDefaultWriteParam();

		// Specifying the required create option
		((JP2GDALKakaduImageWriteParam) param2).setCorder("RPCL");
		pbjImageWrite2.setParameter("writeParam", param2);

		// Writing
		final RenderedOp op2 = JAI.create("ImageWrite", pbjImageWrite2);

		// ////////////////////////////////////////////////////////////////////
		//
		// preparing to write (3RD version of the create option test)
		//
		// ////////////////////////////////////////////////////////////////////
		// Setting output and writer
		final ParameterBlockJAI pbjImageWrite3 = new ParameterBlockJAI(
				"ImageWrite");
		pbjImageWrite3.setParameter("Output", new FileImageOutputStreamExtImpl(
				outputFile3));
		ImageWriter writer3 = new JP2GDALKakaduImageWriterSpi()
				.createWriterInstance();
		pbjImageWrite3.setParameter("Writer", writer3);

		// Specifying image source to write
		pbjImageWrite3.addSource(image);
		ImageWriteParam param3 = writer3.getDefaultWriteParam();

		// Specifying the required create option
		((JP2GDALKakaduImageWriteParam) param3).setCorder("LRCP");
		pbjImageWrite3.setParameter("writeParam", param3);

		// Writing
		final RenderedOp op3 = JAI.create("ImageWrite", pbjImageWrite3);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Testing "Cblk" Create Option.
	//
	// ////////////////////////////////////////////////////////////////////////
	public void testWrite_Cblk() throws IOException, FileNotFoundException {

		LOGGER.info("Testing JP2 Write operation with Cblk option setting");
		// //
		// Preparing input/output files
		// //
		final File inputFile = TestData.file(this, testFileName);
		assertTrue(inputFile.exists());

		// Output files resulting from 2 different values of the same create
		// option. In this test, the create option is Cblk.
		final File outputFile1 = TestData.temp(this, "cblk16x16-.jp2",
				deleteTempFilesOnExit);
		final File outputFile2 = TestData.temp(this, "cblk64x64-.jp2",
				deleteTempFilesOnExit);

		// //
		// Preparing to read
		// //
		final ParameterBlockJAI pbjImageRead = new ParameterBlockJAI(
				"ImageRead");
		pbjImageRead.setParameter("Input", inputFile);
		if (ENABLE_SUBSAMPLING) {
			ImageReadParam readParam = new ImageReadParam();
			readParam.setSourceSubsampling(4, 4, 0, 0);
			pbjImageRead.setParameter("readParam", readParam);
		}
		pbjImageRead.setParameter("Reader", new JP2GDALKakaduImageReaderSpi()
				.createReaderInstance());
		RenderedOp image = JAI.create("ImageRead", pbjImageRead);

		// ////////////////////////////////////////////////////////////////////
		//
		// preparing to write (1ST version of the create option test)
		//
		// ////////////////////////////////////////////////////////////////////
		// Setting output and writer
		final ParameterBlockJAI pbjImageWrite = new ParameterBlockJAI(
				"ImageWrite");
		pbjImageWrite.setParameter("Output", new FileImageOutputStreamExtImpl(
				outputFile1));
		ImageWriter writer = new JP2GDALKakaduImageWriterSpi()
				.createWriterInstance();
		pbjImageWrite.setParameter("Writer", writer);

		// Specifying image source to write
		pbjImageWrite.addSource(image);
		ImageWriteParam param = writer.getDefaultWriteParam();

		// Specifying the required create option
		((JP2GDALKakaduImageWriteParam) param).setCblk("{16,16}");
		pbjImageWrite.setParameter("writeParam", param);

		// Writing
		final RenderedOp op = JAI.create("ImageWrite", pbjImageWrite);

		// ////////////////////////////////////////////////////////////////////
		//
		// preparing to write (2ND version of the create option test)
		//
		// ////////////////////////////////////////////////////////////////////
		// Setting output and writer
		final ParameterBlockJAI pbjImageWrite2 = new ParameterBlockJAI(
				"ImageWrite");
		pbjImageWrite2.setParameter("Output", new FileImageOutputStreamExtImpl(
				outputFile2));
		ImageWriter writer2 = new JP2GDALKakaduImageWriterSpi()
				.createWriterInstance();
		pbjImageWrite2.setParameter("Writer", writer2);

		// Specifying image source to write
		pbjImageWrite2.addSource(image);
		ImageWriteParam param2 = writer2.getDefaultWriteParam();

		// Specifying the required create option
		((JP2GDALKakaduImageWriteParam) param2).setCblk("{64,64}");
		pbjImageWrite2.setParameter("writeParam", param2);

		// Writing
		final RenderedOp op2 = JAI.create("ImageWrite", pbjImageWrite2);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Testing "CModes" Create Option.
	//
	// ////////////////////////////////////////////////////////////////////////

	public void testWrite_Cmodes() throws IOException, FileNotFoundException {

		LOGGER.info("Testing JP2 Write operation with CModes option setting");
		// //
		// Preparing input/output files
		// //
		final File inputFile = TestData.file(this, testFileName);
		assertTrue(inputFile.exists());

		// //
		// Preparing to read
		// //
		final ParameterBlockJAI pbjImageRead = new ParameterBlockJAI(
				"ImageRead");
		pbjImageRead.setParameter("Input", inputFile);
		if (ENABLE_SUBSAMPLING) {
			ImageReadParam readParam = new ImageReadParam();
			readParam.setSourceSubsampling(4, 4, 0, 0);
			pbjImageRead.setParameter("readParam", readParam);
		}
		pbjImageRead.setParameter("Reader", new JP2GDALKakaduImageReaderSpi()
				.createReaderInstance());
		RenderedOp image = JAI.create("ImageRead", pbjImageRead);

		// ////////////////////////////////////////////////////////////////////
		//
		// preparing to write (i-TH version of the create option test)
		//
		// ////////////////////////////////////////////////////////////////////
		final String[] createVersions = { "BYPASS", "BYPASS|RESTART|CAUSAL",
				"RESTART|ERTERM", "RESET" };
		final String[] filenameVersions = { "BYPASS", "BYPASSRESTARTCAUSAL",
				"RESTARTERTERM", "RESET" };

		final int numberOfVersions = createVersions.length;
		for (int i = 0; i < numberOfVersions; i++) {

			// Output files resulting from different values of the same create
			// option. In this test, the create option is ORGtparts.
			final String filenameVersion = filenameVersions[i];
			final StringBuffer fileName = new StringBuffer("CModes").append(
					filenameVersion).append(".jp2");
			final File outputFile = TestData.temp(this, fileName.toString(),
					deleteTempFilesOnExit);

			// Setting output and writer
			final ParameterBlockJAI pbjImageWrite = new ParameterBlockJAI(
					"ImageWrite");
			pbjImageWrite.setParameter("Output",
					new FileImageOutputStreamExtImpl(outputFile));
			ImageWriter writer = new JP2GDALKakaduImageWriterSpi()
					.createWriterInstance();
			pbjImageWrite.setParameter("Writer", writer);

			// Specifying image source to write
			pbjImageWrite.addSource(image);
			ImageWriteParam param = writer.getDefaultWriteParam();

			// Specifying the required create option
			((JP2GDALKakaduImageWriteParam) param).setCmodes(createVersions[i]);

			pbjImageWrite.setParameter("writeParam", param);

			// Writing
			final RenderedOp op = JAI.create("ImageWrite", pbjImageWrite);
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Testing "Cycc" Create Option.
	//
	// ////////////////////////////////////////////////////////////////////////
	public void testWrite_Cycc() throws IOException, FileNotFoundException {

		LOGGER.info("Testing JP2 Write operation with Cycc option setting");
		// //
		// Preparing input/output files
		// //
		final File inputFile = TestData.file(this, testFileName);
		assertTrue(inputFile.exists());

		// Output files resulting from 2 different values of the same create
		// option. In this test, the create option is ORGgen_plt.
		final File outputFile1 = TestData.temp(this, "Cycc-Y-.jp2",
				deleteTempFilesOnExit);
		final File outputFile2 = TestData.temp(this, "Cycc-N-.jp2",
				deleteTempFilesOnExit);

		// //
		// Preparing to read
		// //
		final ParameterBlockJAI pbjImageRead = new ParameterBlockJAI(
				"ImageRead");
		pbjImageRead.setParameter("Input", inputFile);
		if (ENABLE_SUBSAMPLING) {
			ImageReadParam readParam = new ImageReadParam();
			readParam.setSourceSubsampling(4, 4, 0, 0);
			pbjImageRead.setParameter("readParam", readParam);
		}
		pbjImageRead.setParameter("Reader", new JP2GDALKakaduImageReaderSpi()
				.createReaderInstance());
		RenderedOp image = JAI.create("ImageRead", pbjImageRead);

		// ////////////////////////////////////////////////////////////////////
		//
		// preparing to write (1ST version of the create option test)
		//
		// ////////////////////////////////////////////////////////////////////
		// Setting output and writer
		final ParameterBlockJAI pbjImageWrite = new ParameterBlockJAI(
				"ImageWrite");
		pbjImageWrite.setParameter("Output", new FileImageOutputStreamExtImpl(
				outputFile1));
		ImageWriter writer = new JP2GDALKakaduImageWriterSpi()
				.createWriterInstance();
		pbjImageWrite.setParameter("Writer", writer);

		// Specifying image source to write
		pbjImageWrite.addSource(image);
		ImageWriteParam param = writer.getDefaultWriteParam();

		// Specifying the required create option
		((JP2GDALKakaduImageWriteParam) param).setCycc("yes");
		pbjImageWrite.setParameter("writeParam", param);

		// Writing
		final RenderedOp op = JAI.create("ImageWrite", pbjImageWrite);

		// ////////////////////////////////////////////////////////////////////
		//
		// preparing to write (2ND version of the create option test)
		//
		// ////////////////////////////////////////////////////////////////////
		// Setting output and writer
		final ParameterBlockJAI pbjImageWrite2 = new ParameterBlockJAI(
				"ImageWrite");
		pbjImageWrite2.setParameter("Output", new FileImageOutputStreamExtImpl(
				outputFile2));
		ImageWriter writer2 = new JP2GDALKakaduImageWriterSpi()
				.createWriterInstance();
		pbjImageWrite2.setParameter("Writer", writer2);

		// Specifying image source to write
		pbjImageWrite2.addSource(image);
		ImageWriteParam param2 = writer2.getDefaultWriteParam();

		// Specifying the required create option
		((JP2GDALKakaduImageWriteParam) param2).setCycc("no");
		pbjImageWrite2.setParameter("writeParam", param2);

		// Writing
		final RenderedOp op2 = JAI.create("ImageWrite", pbjImageWrite2);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Testing "GMLJp2" Create Option.
	//
	// ////////////////////////////////////////////////////////////////////////
	public void testWrite_GMLJp2() throws IOException, FileNotFoundException {

		LOGGER.info("Testing JP2 Write operation with GMLJp2 option setting");
		// //
		// Preparing input/output files
		// //
		final File inputFile = TestData.file(this, "bogota.jp2");
		assertTrue(inputFile.exists());

		// Output files resulting from 2 different values of the same create
		// option. In this test, the create option is GMLJp2.
		final File outputFile1 = TestData.temp(this, "GML-.jp2",
				deleteTempFilesOnExit);
		final File outputFile2 = TestData.temp(this, "NO-GML-.jp2",
				deleteTempFilesOnExit);

		// //
		// Preparing to read
		// //
		final ParameterBlockJAI pbjImageRead = new ParameterBlockJAI(
				"ImageRead");
		pbjImageRead.setParameter("Input", inputFile);
		if (ENABLE_SUBSAMPLING) {
			ImageReadParam readParam = new ImageReadParam();
			readParam.setSourceSubsampling(4, 4, 0, 0);
			pbjImageRead.setParameter("readParam", readParam);
		}
		pbjImageRead.setParameter("Reader", new JP2GDALKakaduImageReaderSpi()
				.createReaderInstance());
		RenderedOp image = JAI.create("ImageRead", pbjImageRead);

		// ////////////////////////////////////////////////////////////////////
		//
		// preparing to write (1ST version of the create option test)
		//
		// ////////////////////////////////////////////////////////////////////
		// Setting output and writer
		final ParameterBlockJAI pbjImageWrite = new ParameterBlockJAI(
				"ImageWrite");
		pbjImageWrite.setParameter("Output", new FileImageOutputStreamExtImpl(
				outputFile1));
		ImageWriter writer = new JP2GDALKakaduImageWriterSpi()
				.createWriterInstance();
		pbjImageWrite.setParameter("Writer", writer);

		// Specifying image source to write
		pbjImageWrite.addSource(image);
		ImageWriteParam param = writer.getDefaultWriteParam();

		// Specifying the required create option
		((JP2GDALKakaduImageWriteParam) param).setGMLJp2("YES");
		pbjImageWrite.setParameter("writeParam", param);

		// Writing
		final RenderedOp op = JAI.create("ImageWrite", pbjImageWrite);

		// ////////////////////////////////////////////////////////////////////
		//
		// preparing to write (2ND version of the create option test)
		//
		// ////////////////////////////////////////////////////////////////////
		// Setting output and writer
		final ParameterBlockJAI pbjImageWrite2 = new ParameterBlockJAI(
				"ImageWrite");
		pbjImageWrite2.setParameter("Output", new FileImageOutputStreamExtImpl(
				outputFile2));
		ImageWriter writer2 = new JP2GDALKakaduImageWriterSpi()
				.createWriterInstance();
		pbjImageWrite2.setParameter("Writer", writer2);

		// Specifying image source to write
		pbjImageWrite2.addSource(image);
		ImageWriteParam param2 = writer2.getDefaultWriteParam();

		// Specifying the required create option
		((JP2GDALKakaduImageWriteParam) param2).setGMLJp2("NO");
		pbjImageWrite2.setParameter("writeParam", param2);

		// Writing
		final RenderedOp op2 = JAI.create("ImageWrite", pbjImageWrite2);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Testing "GeoJp2" Create Option.
	//
	// ////////////////////////////////////////////////////////////////////////
	public void testWrite_GeoJp2() throws IOException, FileNotFoundException {

		LOGGER.info("Testing JP2 Write operation with GeoJp2 option setting");
		// //
		// Preparing input/output files
		// //
		final File inputFile = TestData.file(this, "bogota.jp2");
		assertTrue(inputFile.exists());

		// Output files resulting from 2 different values of the same create
		// option. In this test, the create option is GeoJp2.
		final File outputFile1 = TestData.temp(this, "GeoJp2-.jp2",
				deleteTempFilesOnExit);
		final File outputFile2 = TestData.temp(this, "NO-GeoJp2-.jp2",
				deleteTempFilesOnExit);

		// //
		// Preparing to read
		// //
		final ParameterBlockJAI pbjImageRead = new ParameterBlockJAI(
				"ImageRead");
		pbjImageRead.setParameter("Input", inputFile);
		if (ENABLE_SUBSAMPLING) {
			ImageReadParam readParam = new ImageReadParam();
			readParam.setSourceSubsampling(4, 4, 0, 0);
			pbjImageRead.setParameter("readParam", readParam);
		}
		pbjImageRead.setParameter("Reader", new JP2GDALKakaduImageReaderSpi()
				.createReaderInstance());
		RenderedOp image = JAI.create("ImageRead", pbjImageRead);

		// ////////////////////////////////////////////////////////////////////
		//
		// preparing to write (1ST version of the create option test)
		//
		// ////////////////////////////////////////////////////////////////////
		// Setting output and writer
		final ParameterBlockJAI pbjImageWrite = new ParameterBlockJAI(
				"ImageWrite");
		pbjImageWrite.setParameter("Output", new FileImageOutputStreamExtImpl(
				outputFile1));
		ImageWriter writer = new JP2GDALKakaduImageWriterSpi()
				.createWriterInstance();
		pbjImageWrite.setParameter("Writer", writer);

		// Specifying image source to write
		pbjImageWrite.addSource(image);
		ImageWriteParam param = writer.getDefaultWriteParam();

		// Specifying the required create option
		((JP2GDALKakaduImageWriteParam) param).setGeoJp2("YES");
		pbjImageWrite.setParameter("writeParam", param);

		// Writing
		final RenderedOp op = JAI.create("ImageWrite", pbjImageWrite);

		// ////////////////////////////////////////////////////////////////////
		//
		// preparing to write (2ND version of the create option test)
		//
		// ////////////////////////////////////////////////////////////////////
		// Setting output and writer
		final ParameterBlockJAI pbjImageWrite2 = new ParameterBlockJAI(
				"ImageWrite");
		pbjImageWrite2.setParameter("Output", new FileImageOutputStreamExtImpl(
				outputFile2));
		ImageWriter writer2 = new JP2GDALKakaduImageWriterSpi()
				.createWriterInstance();
		pbjImageWrite2.setParameter("Writer", writer2);

		// Specifying image source to write
		pbjImageWrite2.addSource(image);
		ImageWriteParam param2 = writer2.getDefaultWriteParam();

		// Specifying the required create option
		((JP2GDALKakaduImageWriteParam) param2).setGeoJp2("NO");
		pbjImageWrite2.setParameter("writeParam", param2);

		// Writing
		final RenderedOp op2 = JAI.create("ImageWrite", pbjImageWrite2);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Testing "ORGtparts" Create Option.
	//
	// ////////////////////////////////////////////////////////////////////////

	public void testWrite_ORGtparts() throws IOException, FileNotFoundException {

		LOGGER
				.info("Testing JP2 Write operation with ORGtparts option setting");
		// //
		// Preparing input/output files
		// //
		final File inputFile = TestData.file(this, testFileName);
		assertTrue(inputFile.exists());

		// //
		// Preparing to read
		// //
		final ParameterBlockJAI pbjImageRead = new ParameterBlockJAI(
				"ImageRead");

		pbjImageRead.setParameter("Input", inputFile);
		if (ENABLE_SUBSAMPLING) {
			ImageReadParam readParam = new ImageReadParam();
			readParam.setSourceSubsampling(8, 8, 0, 0);
			pbjImageRead.setParameter("readParam", readParam);
		}
		pbjImageRead.setParameter("Reader", new JP2GDALKakaduImageReaderSpi()
				.createReaderInstance());
		RenderedOp image = JAI.create("ImageRead", pbjImageRead);

		// ////////////////////////////////////////////////////////////////////
		//
		// preparing to write (i-TH version of the create option test)
		//
		// ////////////////////////////////////////////////////////////////////
		final String[] createVersions = { "R", "C", "L", "R|C", "R|L", "L|C",
				"R|L|C" };
		final String[] filenameVersions = { "R", "C", "L", "RC", "RL", "LC",
				"RLC" };

		final int numberOfVersions = createVersions.length;
		for (int i = 0; i < numberOfVersions; i++) {

			// Output files resulting from different values of the same create
			// option. In this test, the create option is ORGtparts.
			final String filenameVersion = filenameVersions[i];
			final StringBuffer fileName = new StringBuffer("ORGtparts").append(
					filenameVersion).append("-.jp2");
			final File outputFile = TestData.temp(this, fileName.toString(),
					deleteTempFilesOnExit);

			// Setting output and writer
			final ParameterBlockJAI pbjImageWrite = new ParameterBlockJAI(
					"ImageWrite");
			pbjImageWrite.setParameter("Output",
					new FileImageOutputStreamExtImpl(outputFile));
			ImageWriter writer = new JP2GDALKakaduImageWriterSpi()
					.createWriterInstance();
			pbjImageWrite.setParameter("Writer", writer);

			// Specifying image source to write
			pbjImageWrite.addSource(image);
			ImageWriteParam param = writer.getDefaultWriteParam();

			// Specifying the required create option
			((JP2GDALKakaduImageWriteParam) param).setORGgen_plt("yes");
			((JP2GDALKakaduImageWriteParam) param).setLayers(10);
			((JP2GDALKakaduImageWriteParam) param).setCorder("LRCP");
			((JP2GDALKakaduImageWriteParam) param).setTiling(1024, 1024);
			((JP2GDALKakaduImageWriteParam) param)
					.setORGtparts(createVersions[i]);

			pbjImageWrite.setParameter("writeParam", param);

			// Writing
			final RenderedOp op = JAI.create("ImageWrite", pbjImageWrite);
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Testing "ORGgen_plt" Create Option.
	//
	// ////////////////////////////////////////////////////////////////////////
	public void testWrite_ORGgen_plt() throws IOException,
			FileNotFoundException {

		LOGGER
				.info("Testing JP2 Write operation with ORGgen_plt option setting");
		// //
		// Preparing input/output files
		// //
		final File inputFile = TestData.file(this, testFileName);
		assertTrue(inputFile.exists());

		// Output files resulting from 2 different values of the same create
		// option. In this test, the create option is ORGgen_plt.
		final File outputFile1 = TestData.temp(this, "ORGgen_plt-Y-.jp2",
				deleteTempFilesOnExit);
		final File outputFile2 = TestData.temp(this, "ORGgen_plt-N-.jp2",
				deleteTempFilesOnExit);

		// //
		// Preparing to read
		// //
		final ParameterBlockJAI pbjImageRead = new ParameterBlockJAI(
				"ImageRead");
		pbjImageRead.setParameter("Input", inputFile);
		if (ENABLE_SUBSAMPLING) {
			ImageReadParam readParam = new ImageReadParam();
			readParam.setSourceSubsampling(4, 4, 0, 0);
			pbjImageRead.setParameter("readParam", readParam);
		}
		pbjImageRead.setParameter("Reader", new JP2GDALKakaduImageReaderSpi()
				.createReaderInstance());
		RenderedOp image = JAI.create("ImageRead", pbjImageRead);

		// ////////////////////////////////////////////////////////////////////
		//
		// preparing to write (1ST version of the create option test)
		//
		// ////////////////////////////////////////////////////////////////////
		// Setting output and writer
		final ParameterBlockJAI pbjImageWrite = new ParameterBlockJAI(
				"ImageWrite");
		pbjImageWrite.setParameter("Output", new FileImageOutputStreamExtImpl(
				outputFile1));
		ImageWriter writer = new JP2GDALKakaduImageWriterSpi()
				.createWriterInstance();
		pbjImageWrite.setParameter("Writer", writer);

		// Specifying image source to write
		pbjImageWrite.addSource(image);
		ImageWriteParam param = writer.getDefaultWriteParam();

		// Specifying the required create option
		((JP2GDALKakaduImageWriteParam) param).setORGgen_plt("yes");
		pbjImageWrite.setParameter("writeParam", param);

		// Writing
		final RenderedOp op = JAI.create("ImageWrite", pbjImageWrite);

		// ////////////////////////////////////////////////////////////////////
		//
		// preparing to write (2ND version of the create option test)
		//
		// ////////////////////////////////////////////////////////////////////
		// Setting output and writer
		final ParameterBlockJAI pbjImageWrite2 = new ParameterBlockJAI(
				"ImageWrite");
		pbjImageWrite2.setParameter("Output", new FileImageOutputStreamExtImpl(
				outputFile2));
		ImageWriter writer2 = new JP2GDALKakaduImageWriterSpi()
				.createWriterInstance();
		pbjImageWrite2.setParameter("Writer", writer2);

		// Specifying image source to write
		pbjImageWrite2.addSource(image);
		ImageWriteParam param2 = writer2.getDefaultWriteParam();

		// Specifying the required create option
		((JP2GDALKakaduImageWriteParam) param2).setORGgen_plt("no");
		pbjImageWrite2.setParameter("writeParam", param2);

		// Writing
		final RenderedOp op2 = JAI.create("ImageWrite", pbjImageWrite2);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Testing "ORGgen_tlm" Create Option.
	//
	// ////////////////////////////////////////////////////////////////////////

	// Setting this parameter requires a bit of attention. Here below, there is
	// the kakadu documentation related to this paramater:
	// ------------------------------------------------------------------------
	// Requests the insertion of TLM (tile-part-length) marker segments in the
	// main header, to facilitate random access to the code-stream. This
	// attribute takes a single integer-valued parameter, which identifies the
	// maximum number of tile-parts which will be written to the code-stream for
	// each tile. The reason for including this parameter is that space for the
	// TLM information must be reserved ahead of time; once the entire
	// code-stream has been written the generation machinery goes back and
	// overwrites this reserved space with actual TLM data. If the actual number
	// of tile-parts which are generated is less than the value supplied here,
	// empty tile-parts will be inserted into the code-stream so as to use up
	// all of the reserved TLM space. For this reason, you should try to
	// estimate the maximum number of tile-parts you will need as accurately as
	// possible, noting that the actual value may be hard to determine ahead of
	// time if incremental flushing features are to be employed. An error will
	// be generated at run-time if the number of declared maximum number of
	// tile-parts turns out to be insufficient.
	// ------------------------------------------------------------------------
	public void testWrite_ORGgen_tlm() throws IOException,
			FileNotFoundException {

		LOGGER
				.info("Testing JP2 Write operation with ORGgen_tlm option setting");
		// //
		// Preparing input/output files
		// //
		final File inputFile = TestData.file(this, testFileName);
		assertTrue(inputFile.exists());

		final File outputFile1 = TestData.temp(this, "ORGgen_tlm0-.jp2",
				deleteTempFilesOnExit);

		// //
		// Preparing to read
		// //
		final ParameterBlockJAI pbjImageRead = new ParameterBlockJAI(
				"ImageRead");
		pbjImageRead.setParameter("Input", inputFile);
		if (ENABLE_SUBSAMPLING) {
			ImageReadParam readParam = new ImageReadParam();
			readParam.setSourceSubsampling(4, 4, 0, 0);
			pbjImageRead.setParameter("readParam", readParam);
		}
		pbjImageRead.setParameter("Reader", new JP2GDALKakaduImageReaderSpi()
				.createReaderInstance());
		RenderedOp image = JAI.create("ImageRead", pbjImageRead);

		// ////////////////////////////////////////////////////////////////////
		//
		// preparing to write
		//
		// ////////////////////////////////////////////////////////////////////
		// Setting output and writer
		final ParameterBlockJAI pbjImageWrite = new ParameterBlockJAI(
				"ImageWrite");
		pbjImageWrite.setParameter("Output", new FileImageOutputStreamExtImpl(
				outputFile1));
		ImageWriter writer = new JP2GDALKakaduImageWriterSpi()
				.createWriterInstance();
		pbjImageWrite.setParameter("Writer", writer);

		// Specifying image source to write
		pbjImageWrite.addSource(image);
		ImageWriteParam param = writer.getDefaultWriteParam();

		// Specifying the required create option
		((JP2GDALKakaduImageWriteParam) param).setORGgen_tlm(0);
		pbjImageWrite.setParameter("writeParam", param);

	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Testing "COMSEG" Create Option.
	//
	// ////////////////////////////////////////////////////////////////////////
	public void testWrite_COMSEG() throws IOException, FileNotFoundException {

		LOGGER.info("Testing JP2 Write operation with COMSEG option setting");
		// //
		// Preparing input/output files
		// //
		final File inputFile = TestData.file(this, testFileName);
		assertTrue(inputFile.exists());

		// Output files resulting from 2 different values of the same create
		// option. In this test, the create option is COMSEG.
		final File outputFile1 = TestData.temp(this, "COMSEG-.jp2",
				deleteTempFilesOnExit);
		final File outputFile2 = TestData.temp(this, "NO-COMSEG-.jp2",
				deleteTempFilesOnExit);

		// //
		// Preparing to read
		// //
		final ParameterBlockJAI pbjImageRead = new ParameterBlockJAI(
				"ImageRead");
		pbjImageRead.setParameter("Input", inputFile);
		if (ENABLE_SUBSAMPLING) {
			ImageReadParam readParam = new ImageReadParam();
			readParam.setSourceSubsampling(4, 4, 0, 0);
			pbjImageRead.setParameter("readParam", readParam);
		}
		pbjImageRead.setParameter("Reader", new JP2GDALKakaduImageReaderSpi()
				.createReaderInstance());
		RenderedOp image = JAI.create("ImageRead", pbjImageRead);

		// ////////////////////////////////////////////////////////////////////
		//
		// preparing to write (1ST version of the create option test)
		//
		// ////////////////////////////////////////////////////////////////////
		// Setting output and writer
		final ParameterBlockJAI pbjImageWrite = new ParameterBlockJAI(
				"ImageWrite");
		pbjImageWrite.setParameter("Output", new FileImageOutputStreamExtImpl(
				outputFile1));
		ImageWriter writer = new JP2GDALKakaduImageWriterSpi()
				.createWriterInstance();
		pbjImageWrite.setParameter("Writer", writer);

		// Specifying image source to write
		pbjImageWrite.addSource(image);
		ImageWriteParam param = writer.getDefaultWriteParam();

		// Specifying the required create option
		((JP2GDALKakaduImageWriteParam) param).setComseg("YES");
		pbjImageWrite.setParameter("writeParam", param);

		// Writing
		final RenderedOp op = JAI.create("ImageWrite", pbjImageWrite);

		// ////////////////////////////////////////////////////////////////////
		//
		// preparing to write (2ND version of the create option test)
		//
		// ////////////////////////////////////////////////////////////////////
		// Setting output and writer
		final ParameterBlockJAI pbjImageWrite2 = new ParameterBlockJAI(
				"ImageWrite");
		pbjImageWrite2.setParameter("Output", new FileImageOutputStreamExtImpl(
				outputFile2));
		ImageWriter writer2 = new JP2GDALKakaduImageWriterSpi()
				.createWriterInstance();
		pbjImageWrite2.setParameter("Writer", writer2);

		// Specifying image source to write
		pbjImageWrite2.addSource(image);
		ImageWriteParam param2 = writer2.getDefaultWriteParam();

		// Specifying the required create option
		((JP2GDALKakaduImageWriteParam) param2).setComseg("NO");
		pbjImageWrite2.setParameter("writeParam", param2);

		// Writing
		final RenderedOp op2 = JAI.create("ImageWrite", pbjImageWrite2);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Testing "SProfile" Create Option.
	//
	// ////////////////////////////////////////////////////////////////////////
	public void testWrite_SProfile() throws IOException, FileNotFoundException {

		LOGGER.info("Testing JP2 Write operation with SProfile option setting");
		// //
		// Preparing input/output files
		// //
		final File inputFile = TestData.file(this, testFileName);
		assertTrue(inputFile.exists());

		// Output files resulting from 2 different values of the same create
		// option. In this test, the create option is SProfile.
		final File outputFile1 = TestData.temp(this, "SProfile1-.jp2",
				deleteTempFilesOnExit);
		final File outputFile2 = TestData.temp(this, "SProfile2-.jp2",
				deleteTempFilesOnExit);
		// //
		// Preparing to read
		// //
		final ParameterBlockJAI pbjImageRead = new ParameterBlockJAI(
				"ImageRead");
		pbjImageRead.setParameter("Input", inputFile);
		if (ENABLE_SUBSAMPLING) {
			ImageReadParam readParam = new ImageReadParam();
			readParam.setSourceSubsampling(4, 4, 0, 0);
			pbjImageRead.setParameter("readParam", readParam);
		}
		pbjImageRead.setParameter("Reader", new JP2GDALKakaduImageReaderSpi()
				.createReaderInstance());
		RenderedOp image = JAI.create("ImageRead", pbjImageRead);

		// ////////////////////////////////////////////////////////////////////
		//
		// preparing to write (1ST version of the create option test)
		//
		// ////////////////////////////////////////////////////////////////////
		// Setting output and writer
		final ParameterBlockJAI pbjImageWrite = new ParameterBlockJAI(
				"ImageWrite");
		pbjImageWrite.setParameter("Output", new FileImageOutputStreamExtImpl(
				outputFile1));
		ImageWriter writer = new JP2GDALKakaduImageWriterSpi()
				.createWriterInstance();
		pbjImageWrite.setParameter("Writer", writer);

		// Specifying image source to write
		pbjImageWrite.addSource(image);
		ImageWriteParam param = writer.getDefaultWriteParam();

		// Specifying the required create option
		((JP2GDALKakaduImageWriteParam) param).setSProfile(1);
		pbjImageWrite.setParameter("writeParam", param);

		// Writing
		final RenderedOp op = JAI.create("ImageWrite", pbjImageWrite);

		// ////////////////////////////////////////////////////////////////////
		//
		// preparing to write (2ND version of the create option test)
		//
		// ////////////////////////////////////////////////////////////////////
		// Setting output and writer
		final ParameterBlockJAI pbjImageWrite2 = new ParameterBlockJAI(
				"ImageWrite");
		pbjImageWrite2.setParameter("Output", new FileImageOutputStreamExtImpl(
				outputFile2));
		ImageWriter writer2 = new JP2GDALKakaduImageWriterSpi()
				.createWriterInstance();
		pbjImageWrite2.setParameter("Writer", writer2);

		// Specifying image source to write
		pbjImageWrite2.addSource(image);
		ImageWriteParam param2 = writer2.getDefaultWriteParam();

		// Specifying the required create option
		((JP2GDALKakaduImageWriteParam) param2).setSProfile("PROFILE2");
		pbjImageWrite2.setParameter("writeParam", param2);

		// Writing
		final RenderedOp op2 = JAI.create("ImageWrite", pbjImageWrite2);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Testing "Tiling" Create Option.
	//
	// ////////////////////////////////////////////////////////////////////////
	public void testWrite_Tiling() throws IOException, FileNotFoundException {

		LOGGER.info("Testing JP2 Write operation with Tiling option setting");
		// //
		// Preparing input/output files
		// //
		final File inputFile = TestData.file(this, testFileName);
		assertTrue(inputFile.exists());

		// Output files resulting from 2 different values of the tiling.
		final int firstTilingParam = 128;
		final int secondTilingParam = 256;
		final String fileName1 = new StringBuffer("Tiled-").append(
				Integer.toString(firstTilingParam)).append("-.jp2").toString();
		final String fileName2 = new StringBuffer("Tiled-").append(
				Integer.toString(secondTilingParam)).append("-.jp2").toString();

		final File outputFile1 = TestData.temp(this, fileName1,
				deleteTempFilesOnExit);
		final File outputFile2 = TestData.temp(this, fileName2,
				deleteTempFilesOnExit);

		// //
		// Preparing to read
		// //
		final ParameterBlockJAI pbjImageRead = new ParameterBlockJAI(
				"ImageRead");
		pbjImageRead.setParameter("Input", inputFile);
		if (ENABLE_SUBSAMPLING) {
			ImageReadParam readParam = new ImageReadParam();
			readParam.setSourceSubsampling(4, 4, 0, 0);
			pbjImageRead.setParameter("readParam", readParam);
		}
		// Reading
		pbjImageRead.setParameter("Reader", new JP2GDALKakaduImageReaderSpi()
				.createReaderInstance());
		RenderedOp image = JAI.create("ImageRead", pbjImageRead);

		// ////////////////////////////////////////////////////////////////////
		//
		// preparing to write (1ST version of the create option test)
		//
		// ////////////////////////////////////////////////////////////////////
		// Setting output and writer
		final ParameterBlockJAI pbjImageWrite = new ParameterBlockJAI(
				"ImageWrite");
		pbjImageWrite.setParameter("Output", new FileImageOutputStreamExtImpl(
				outputFile1));
		ImageWriter writer = new JP2GDALKakaduImageWriterSpi()
				.createWriterInstance();
		pbjImageWrite.setParameter("Writer", writer);

		// Specifying image source to write
		pbjImageWrite.addSource(image);
		ImageWriteParam param = writer.getDefaultWriteParam();

		// Specifying the required create option
		((JP2GDALKakaduImageWriteParam) param).setTiling(firstTilingParam,
				firstTilingParam);
		pbjImageWrite.setParameter("writeParam", param);

		// Writing
		final RenderedOp op = JAI.create("ImageWrite", pbjImageWrite);

		// ////////////////////////////////////////////////////////////////////
		//
		// preparing to write (2ND version of the create option test)
		//
		// ////////////////////////////////////////////////////////////////////
		// Setting output and writer
		final ParameterBlockJAI pbjImageWrite2 = new ParameterBlockJAI(
				"ImageWrite");
		pbjImageWrite2.setParameter("Output", new FileImageOutputStreamExtImpl(
				outputFile2));
		ImageWriter writer2 = new JP2GDALKakaduImageWriterSpi()
				.createWriterInstance();
		pbjImageWrite2.setParameter("Writer", writer2);

		// Specifying image source to write
		pbjImageWrite2.addSource(image);
		ImageWriteParam param2 = writer2.getDefaultWriteParam();

		// Specifying the required create option
		((JP2GDALKakaduImageWriteParam) param2).setTiling(secondTilingParam,
				secondTilingParam);
		pbjImageWrite2.setParameter("writeParam", param2);

		// Writing
		final RenderedOp op2 = JAI.create("ImageWrite", pbjImageWrite2);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Testing "ROI" Create Options and related options.
	//
	// ////////////////////////////////////////////////////////////////////////
	public void testWrite_ROI() throws IOException, FileNotFoundException {

		LOGGER.info("Testing JP2 Write operation with ROI option setting");
		// //
		// Preparing input/output files
		// //
		final File inputFile = TestData.file(this, testFileName);
		assertTrue(inputFile.exists());

		// Output files resulting from 2 different values of the same create
		// option. In this test, the create option is SProfile.
		final File outputFile1 = TestData.temp(this, "ROI-NO_ROI-.jp2",
				deleteTempFilesOnExit);
		final File outputFile2 = TestData.temp(this, "ROI-parametrized-.jp2",
				deleteTempFilesOnExit);

		// //
		// Preparing to read
		// //
		final ParameterBlockJAI pbjImageRead = new ParameterBlockJAI(
				"ImageRead");

		pbjImageRead.setParameter("Input", inputFile);
		if (ENABLE_SUBSAMPLING) {
			ImageReadParam readParam = new ImageReadParam();
			readParam.setSourceSubsampling(4, 4, 0, 0);
			pbjImageRead.setParameter("readParam", readParam);
		}
		pbjImageRead.setParameter("Reader", new JP2GDALKakaduImageReaderSpi()
				.createReaderInstance());
		RenderedOp image = JAI.create("ImageRead", pbjImageRead);

		// ////////////////////////////////////////////////////////////////////
		//
		// preparing to write (1ST version of the create option test)
		//
		// ////////////////////////////////////////////////////////////////////
		// Setting output and writer
		final ParameterBlockJAI pbjImageWrite = new ParameterBlockJAI(
				"ImageWrite");
		pbjImageWrite.setParameter("Output", new FileImageOutputStreamExtImpl(
				outputFile1));
		ImageWriter writer = new JP2GDALKakaduImageWriterSpi()
				.createWriterInstance();
		pbjImageWrite.setParameter("Writer", writer);

		// Specifying image source to write
		pbjImageWrite.addSource(image);
		ImageWriteParam param = writer.getDefaultWriteParam();

		// Specifying the required create option
		pbjImageWrite.setParameter("writeParam", param);

		// Writing
		final RenderedOp op = JAI.create("ImageWrite", pbjImageWrite);

		// ////////////////////////////////////////////////////////////////////
		//
		// preparing to write (2ND version of the create option test)
		//
		// ////////////////////////////////////////////////////////////////////

		// Setting output and writer
		final ParameterBlockJAI pbjImageWrite2 = new ParameterBlockJAI(
				"ImageWrite");
		pbjImageWrite2.setParameter("Output", new FileImageOutputStreamExtImpl(
				outputFile2));
		ImageWriter writer2 = new JP2GDALKakaduImageWriterSpi()
				.createWriterInstance();
		pbjImageWrite2.setParameter("Writer", writer2);

		// Specifying image source to write
		pbjImageWrite2.addSource(image);
		ImageWriteParam param2 = writer2.getDefaultWriteParam();

		// Specifying the required create option
		((JP2GDALKakaduImageWriteParam) param2).setClayers(20);
		((JP2GDALKakaduImageWriteParam) param2).setROI("400,400,400,400");
		((JP2GDALKakaduImageWriteParam) param2).setRweight(64000f);
		((JP2GDALKakaduImageWriteParam) param2)
				.setCprecincts("{256,256},{128,128},{64,64},{32,32}");

		pbjImageWrite2.setParameter("writeParam", param2);

		// Writing
		final RenderedOp op2 = JAI.create("ImageWrite", pbjImageWrite2);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Testing "Qguard" Create Option.
	//
	// ////////////////////////////////////////////////////////////////////////
	public void testWrite_Qguard() throws IOException, FileNotFoundException {

		LOGGER.info("Testing JP2 Write operation with Qguard option setting");
		// //
		// Preparing input/output files
		// //
		final File inputFile = TestData.file(this, testFileName);
		assertTrue(inputFile.exists());

		// Output files resulting from 2 different values of the same create
		// option. In this test, the create option is Qguard.
		final File outputFile1 = TestData.temp(this, "Qguard1-.jp2",
				deleteTempFilesOnExit);
		final File outputFile2 = TestData.temp(this, "Qguard2-.jp2",
				deleteTempFilesOnExit);

		// //
		// Preparing to read
		// //
		final ParameterBlockJAI pbjImageRead = new ParameterBlockJAI(
				"ImageRead");
		pbjImageRead.setParameter("Input", inputFile);
		if (ENABLE_SUBSAMPLING) {
			ImageReadParam readParam = new ImageReadParam();
			readParam.setSourceSubsampling(4, 4, 0, 0);
			pbjImageRead.setParameter("readParam", readParam);
		}
		pbjImageRead.setParameter("Reader", new JP2GDALKakaduImageReaderSpi()
				.createReaderInstance());
		RenderedOp image = JAI.create("ImageRead", pbjImageRead);

		// ////////////////////////////////////////////////////////////////////
		//
		// preparing to write (1ST version of the create option test)
		//
		// ////////////////////////////////////////////////////////////////////
		// Setting output and writer
		final ParameterBlockJAI pbjImageWrite = new ParameterBlockJAI(
				"ImageWrite");
		pbjImageWrite.setParameter("Output", new FileImageOutputStreamExtImpl(
				outputFile1));
		ImageWriter writer = new JP2GDALKakaduImageWriterSpi()
				.createWriterInstance();
		pbjImageWrite.setParameter("Writer", writer);

		// Specifying image source to write
		pbjImageWrite.addSource(image);
		ImageWriteParam param = writer.getDefaultWriteParam();

		// Specifying the required create option
		((JP2GDALKakaduImageWriteParam) param).setQguard(1);
		pbjImageWrite.setParameter("writeParam", param);

		// Writing
		final RenderedOp op = JAI.create("ImageWrite", pbjImageWrite);

		// ////////////////////////////////////////////////////////////////////
		//
		// preparing to write (2ND version of the create option test)
		//
		// ////////////////////////////////////////////////////////////////////
		// Setting output and writer
		final ParameterBlockJAI pbjImageWrite2 = new ParameterBlockJAI(
				"ImageWrite");
		pbjImageWrite2.setParameter("Output", new FileImageOutputStreamExtImpl(
				outputFile2));
		ImageWriter writer2 = new JP2GDALKakaduImageWriterSpi()
				.createWriterInstance();
		pbjImageWrite2.setParameter("Writer", writer2);

		// Specifying image source to write
		pbjImageWrite2.addSource(image);
		ImageWriteParam param2 = writer2.getDefaultWriteParam();

		// Specifying the required create option
		((JP2GDALKakaduImageWriteParam) param2).setQguard(2);
		pbjImageWrite2.setParameter("writeParam", param2);

		// Writing
		final RenderedOp op2 = JAI.create("ImageWrite", pbjImageWrite2);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Testing "Qstep" Create Option.
	//
	// ////////////////////////////////////////////////////////////////////////
	public void testWrite_Qstep() throws IOException, FileNotFoundException {

		LOGGER.info("Testing JP2 Write operation with Qstep option setting");
		// //
		// Preparing input/output files
		// //
		final File inputFile = TestData.file(this, testFileName);
		assertTrue(inputFile.exists());

		// Output files resulting from 3 different values of the same create
		// option. In this test, the create option is Qstep.
		final float firstQstepParam = 0.2f;
		final float secondQstepParam = 1.7f;
		final String fileName1 = new StringBuffer("Qstep-").append(
				Float.toString(firstQstepParam)).append("f-.jp2").toString();
		final String fileName2 = new StringBuffer("Qstep-").append(
				Float.toString(secondQstepParam)).append("f-.jp2").toString();
		final String fileName3 = "Qstep-Default-.jp2";
		final File outputFile1 = TestData.temp(this, fileName1,
				deleteTempFilesOnExit);
		final File outputFile2 = TestData.temp(this, fileName2,
				deleteTempFilesOnExit);
		final File outputFile3 = TestData.temp(this, fileName3,
				deleteTempFilesOnExit);

		// //
		// Preparing to read
		// //
		final ParameterBlockJAI pbjImageRead = new ParameterBlockJAI(
				"ImageRead");
		pbjImageRead.setParameter("Input", inputFile);
		if (ENABLE_SUBSAMPLING) {
			ImageReadParam readParam = new ImageReadParam();
			readParam.setSourceSubsampling(4, 4, 0, 0);
			pbjImageRead.setParameter("readParam", readParam);
		}
		pbjImageRead.setParameter("Reader", new JP2GDALKakaduImageReaderSpi()
				.createReaderInstance());
		RenderedOp image = JAI.create("ImageRead", pbjImageRead);

		// ////////////////////////////////////////////////////////////////////
		//
		// preparing to write (1ST version of the create option test)
		//
		// ////////////////////////////////////////////////////////////////////
		// Setting output and writer
		final ParameterBlockJAI pbjImageWrite = new ParameterBlockJAI(
				"ImageWrite");
		pbjImageWrite.setParameter("Output", new FileImageOutputStreamExtImpl(
				outputFile1));
		ImageWriter writer = new JP2GDALKakaduImageWriterSpi()
				.createWriterInstance();
		pbjImageWrite.setParameter("Writer", writer);

		// Specifying image source to write
		pbjImageWrite.addSource(image);
		ImageWriteParam param = writer.getDefaultWriteParam();

		// Specifying the required create option
		((JP2GDALKakaduImageWriteParam) param).setQstep(firstQstepParam);
		pbjImageWrite.setParameter("writeParam", param);

		// Writing
		final RenderedOp op = JAI.create("ImageWrite", pbjImageWrite);

		// ////////////////////////////////////////////////////////////////////
		//
		// preparing to write (2ND version of the create option test)
		//
		// ////////////////////////////////////////////////////////////////////
		// Setting output and writer
		final ParameterBlockJAI pbjImageWrite2 = new ParameterBlockJAI(
				"ImageWrite");
		pbjImageWrite2.setParameter("Output", new FileImageOutputStreamExtImpl(
				outputFile2));
		ImageWriter writer2 = new JP2GDALKakaduImageWriterSpi()
				.createWriterInstance();
		pbjImageWrite2.setParameter("Writer", writer2);

		// Specifying image source to write
		pbjImageWrite2.addSource(image);
		ImageWriteParam param2 = writer2.getDefaultWriteParam();

		// Specifying the required create option
		((JP2GDALKakaduImageWriteParam) param2).setQstep(secondQstepParam);
		pbjImageWrite2.setParameter("writeParam", param2);

		// Writing
		final RenderedOp op2 = JAI.create("ImageWrite", pbjImageWrite2);

		// ////////////////////////////////////////////////////////////////////
		//
		// preparing to write (3ND version of the create option - DEFAULT)
		//
		// ////////////////////////////////////////////////////////////////////
		// Setting output and writer
		final ParameterBlockJAI pbjImageWrite3 = new ParameterBlockJAI(
				"ImageWrite");
		pbjImageWrite3.setParameter("Output", new FileImageOutputStreamExtImpl(
				outputFile3));
		ImageWriter writer3 = new JP2GDALKakaduImageWriterSpi()
				.createWriterInstance();
		pbjImageWrite3.setParameter("Writer", writer3);

		// Specifying image source to write
		pbjImageWrite3.addSource(image);
		ImageWriteParam param3 = writer3.getDefaultWriteParam();

		// Specifying the required create option
		pbjImageWrite3.setParameter("writeParam", param3);

		// Writing
		final RenderedOp op3 = JAI.create("ImageWrite", pbjImageWrite3);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Testing "Quality" Create Option.
	//
	// ////////////////////////////////////////////////////////////////////////
	// TODO: Investigate the strange displaying of a 100% quality factor write
	// of an input UINT16 jpeg2000 image.
	public void testWrite_Quality() throws IOException, FileNotFoundException {

		LOGGER.info("Testing JP2 Write operation with Quality option setting");
		// //
		// Preparing input/output files
		// //
		final File inputFile = TestData.file(this, testFileName);
		assertTrue(inputFile.exists());

		// Output files resulting from 2 different values of the same create
		// option. In this test, the create option is Qstep.
		final float firstQualityParam = 99.2f;
		final float secondQualityParam = 1f;
		final String fileName1 = new StringBuffer("Quality-").append(
				Float.toString(firstQualityParam)).append("f-.jp2").toString();
		final String fileName2 = new StringBuffer("Quality-").append(
				Float.toString(secondQualityParam)).append("f-.jp2").toString();
		final File outputFile1 = TestData.temp(this, fileName1,
				deleteTempFilesOnExit);
		final File outputFile2 = TestData.temp(this, fileName2,
				deleteTempFilesOnExit);

		// //
		// Preparing to read
		// //
		final ParameterBlockJAI pbjImageRead = new ParameterBlockJAI(
				"ImageRead");
		pbjImageRead.setParameter("Input", inputFile);
		if (ENABLE_SUBSAMPLING) {
			ImageReadParam readParam = new ImageReadParam();
			readParam.setSourceSubsampling(4, 4, 0, 0);
			pbjImageRead.setParameter("readParam", readParam);
		}
		pbjImageRead.setParameter("Reader", new JP2GDALKakaduImageReaderSpi()
				.createReaderInstance());
		RenderedOp image = JAI.create("ImageRead", pbjImageRead);

		// ////////////////////////////////////////////////////////////////////
		//
		// preparing to write (1ST version of the create option test)
		//
		// ////////////////////////////////////////////////////////////////////
		// Setting output and writer
		final ParameterBlockJAI pbjImageWrite = new ParameterBlockJAI(
				"ImageWrite");
		pbjImageWrite.setParameter("Output", new FileImageOutputStreamExtImpl(
				outputFile1));
		ImageWriter writer = new JP2GDALKakaduImageWriterSpi()
				.createWriterInstance();
		pbjImageWrite.setParameter("Writer", writer);

		// Specifying image source to write
		pbjImageWrite.addSource(image);
		ImageWriteParam param = writer.getDefaultWriteParam();

		// Specifying the required create option
		((JP2GDALKakaduImageWriteParam) param).setQuality(firstQualityParam);
		pbjImageWrite.setParameter("writeParam", param);

		// Writing
		final RenderedOp op = JAI.create("ImageWrite", pbjImageWrite);

		// ////////////////////////////////////////////////////////////////////
		//
		// preparing to write (2ND version of the create option test)
		//
		// ////////////////////////////////////////////////////////////////////
		// Setting output and writer
		final ParameterBlockJAI pbjImageWrite2 = new ParameterBlockJAI(
				"ImageWrite");
		pbjImageWrite2.setParameter("Output", new FileImageOutputStreamExtImpl(
				outputFile2));
		ImageWriter writer2 = new JP2GDALKakaduImageWriterSpi()
				.createWriterInstance();
		pbjImageWrite2.setParameter("Writer", writer2);

		// Specifying image source to write
		pbjImageWrite2.addSource(image);
		ImageWriteParam param2 = writer2.getDefaultWriteParam();

		// Specifying the required create option
		((JP2GDALKakaduImageWriteParam) param2).setQuality(secondQualityParam);
		pbjImageWrite2.setParameter("writeParam", param2);

		// Writing
		final RenderedOp op2 = JAI.create("ImageWrite", pbjImageWrite2);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Testing "FLUSH" Create Option.
	//
	// ////////////////////////////////////////////////////////////////////////
	public void testWrite_FLUSH() throws IOException, FileNotFoundException {

		LOGGER.info("Testing JP2 Write operation with FLUSH option setting");
		// //
		// Preparing input/output files
		// //
		final File inputFile = TestData.file(this, testFileName);
		assertTrue(inputFile.exists());

		// Output files resulting from 2 different values of the same create
		// option. In this test, the create option is FLUSH.
		final File outputFile1 = TestData.temp(this, "FLUSH-.jp2",
				deleteTempFilesOnExit);
		final File outputFile2 = TestData.temp(this, "NO-FLUSH-.jp2",
				deleteTempFilesOnExit);

		// //
		// Preparing to read
		// //
		final ParameterBlockJAI pbjImageRead = new ParameterBlockJAI(
				"ImageRead");
		pbjImageRead.setParameter("Input", inputFile);
		if (ENABLE_SUBSAMPLING) {
			ImageReadParam readParam = new ImageReadParam();
			readParam.setSourceSubsampling(4, 4, 0, 0);
			pbjImageRead.setParameter("readParam", readParam);
		}
		pbjImageRead.setParameter("Reader", new JP2GDALKakaduImageReaderSpi()
				.createReaderInstance());
		RenderedOp image = JAI.create("ImageRead", pbjImageRead);

		// ////////////////////////////////////////////////////////////////////
		//
		// preparing to write (1ST version of the create option test)
		//
		// ////////////////////////////////////////////////////////////////////
		// Setting output and writer
		final ParameterBlockJAI pbjImageWrite = new ParameterBlockJAI(
				"ImageWrite");
		pbjImageWrite.setParameter("Output", new FileImageOutputStreamExtImpl(
				outputFile1));
		ImageWriter writer = new JP2GDALKakaduImageWriterSpi()
				.createWriterInstance();
		pbjImageWrite.setParameter("Writer", writer);

		// Specifying image source to write
		pbjImageWrite.addSource(image);
		ImageWriteParam param = writer.getDefaultWriteParam();

		// Specifying the required create option
		((JP2GDALKakaduImageWriteParam) param).setFlush("YES");
		pbjImageWrite.setParameter("writeParam", param);

		// Writing
		final RenderedOp op = JAI.create("ImageWrite", pbjImageWrite);

		// ////////////////////////////////////////////////////////////////////
		//
		// preparing to write (2ND version of the create option test)
		//
		// ////////////////////////////////////////////////////////////////////
		// Setting output and writer
		final ParameterBlockJAI pbjImageWrite2 = new ParameterBlockJAI(
				"ImageWrite");
		pbjImageWrite2.setParameter("Output", new FileImageOutputStreamExtImpl(
				outputFile2));
		ImageWriter writer2 = new JP2GDALKakaduImageWriterSpi()
				.createWriterInstance();
		pbjImageWrite2.setParameter("Writer", writer2);

		// Specifying image source to write
		pbjImageWrite2.addSource(image);
		ImageWriteParam param2 = writer2.getDefaultWriteParam();

		// Specifying the required create option
		((JP2GDALKakaduImageWriteParam) param2).setFlush("NO");
		pbjImageWrite2.setParameter("writeParam", param2);

		// Writing
		final RenderedOp op2 = JAI.create("ImageWrite", pbjImageWrite2);
	}

	public static Test suite() {
		TestSuite suite = new TestSuite();

		if (TestData.isExtensiveTest()) {
			// Testing "Clevels" Create Option.
			suite.addTest(new JP2KWriteTest("testWrite_Clevels"));

			// Testing "Clayers" Create Option.
			suite.addTest(new JP2KWriteTest("testWrite_Clayers"));

			// Testing "Cprecincts" Create Option.
			suite.addTest(new JP2KWriteTest("testWrite_Cprecincts"));

			// Testing "Corder" Create Option.
			suite.addTest(new JP2KWriteTest("testWrite_Corder"));

			// Testing "Cblk" Create Option.
			suite.addTest(new JP2KWriteTest("testWrite_Cblk"));

			// Testing "Cycc" Create Option.
			suite.addTest(new JP2KWriteTest("testWrite_Cycc"));

			// Testing "Cmodes" Create Option.
			suite.addTest(new JP2KWriteTest("testWrite_Cmodes"));

			// Testing "SProfile" Create Option.
			suite.addTest(new JP2KWriteTest("testWrite_SProfile"));

			// Testing "Quality" Create Option.
			suite.addTest(new JP2KWriteTest("testWrite_Quality"));

			// Testing "ORGtparts" Create Option.
			suite.addTest(new JP2KWriteTest("testWrite_ORGtparts"));

			// Testing "ORGgen_plt" Create Option.
			suite.addTest(new JP2KWriteTest("testWrite_ORGgen_plt"));

			// Testing "ORGgen_tlm" Create Option.
			suite.addTest(new JP2KWriteTest("testWrite_ORGgen_tlm"));

			// Testing "GMLJp2" Create Option.
			suite.addTest(new JP2KWriteTest("testWrite_GMLJp2"));

			// Testing "GeoJp2" Create Option.
			suite.addTest(new JP2KWriteTest("testWrite_GeoJp2"));

			// Testing "ROI" Create Option.
			suite.addTest(new JP2KWriteTest("testWrite_ROI"));

			// Testing "Qguard" Create Option.
			suite.addTest(new JP2KWriteTest("testWrite_Qguard"));

			// Testing "Qstep" Create Option.
			suite.addTest(new JP2KWriteTest("testWrite_Qstep"));

			// Testing "COMSEG" Create Option.
			suite.addTest(new JP2KWriteTest("testWrite_COMSEG"));

			// Testing "FLUSH" Create Option.
			suite.addTest(new JP2KWriteTest("testWrite_FLUSH"));

			// Testing "Tiling"
			suite.addTest(new JP2KWriteTest("testWrite_Tiling"));

		}

		suite.addTest(new JP2KWriteTest("testWrite_Parametrized"));
		
		if (!deleteTempFilesOnExit) {
			final String message = new StringBuffer(
					"------------------------------------------------------------------------\n")
					.append(
							"Files written during write operations tests will not be automatically deleted.\n")
					.append("Several tests will reduce you free disk space.\n")
					.append(
							"Be sure of deleting written test data files when they are no more needed.\n")
					.append(
							"------------------------------------------------------------------------------\n")
					.toString();
			LOGGER.info(message);
		}
		
		return suite;
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Testing parameters-support capabilities for writer.
	//
	// ////////////////////////////////////////////////////////////////////////
	public void testWrite_Parametrized() throws IOException,
			FileNotFoundException {

		final File outputFile = TestData.temp(this, "writetest.jp2", false);
		outputFile.deleteOnExit();
		final File inputFile = TestData.file(this, "sampleless.jp2");
		assertTrue(inputFile.exists());

		// ////////////////////////////////////////////////////////////////////
		//
		// Reading
		//
		// ////////////////////////////////////////////////////////////////////
		final ParameterBlockJAI pbjImageRead = new ParameterBlockJAI(
				"ImageRead");
		ImageReader reader = new JP2GDALKakaduImageReaderSpi()
				.createReaderInstance();
		ImageReadParam param = new ImageReadParam();
		param.setSourceSubsampling(2, 2, 0, 0);
		pbjImageRead.setParameter("readParam", param);
		pbjImageRead.setParameter("Input", inputFile);
		pbjImageRead.setParameter("reader", reader);
		RenderedOp image = JAI.create("ImageRead", pbjImageRead);
		image.getRendering();
		if (TestData.isInteractiveTest())
			Viewer.visualize(image, "First Read Image");
		else
			image.getTiles();
		// ////////////////////////////////////////////////////////////////////
		//
		// preparing to write
		//
		// ////////////////////////////////////////////////////////////////////
		// Setting output and writer
		final ParameterBlockJAI pbjImageWrite = new ParameterBlockJAI(
				"ImageWrite");
		pbjImageWrite.setParameter("Output", outputFile);

		ImageWriter writer = new JP2GDALKakaduImageWriterSpi()
				.createWriterInstance();
		pbjImageWrite.addSource(image);
		ImageWriteParam param2 = writer.getDefaultWriteParam();
		pbjImageWrite.setParameter("writer", writer);
		pbjImageWrite.setParameter("ImageMetadata", reader.getImageMetadata(0));
		pbjImageWrite.setParameter("Transcode", false);

		param2.setSourceRegion(new Rectangle(100, 100, 500, 500));
		param2.setSourceSubsampling(2, 3, 0, 0);

		pbjImageWrite.setParameter("writeParam", param2);

		// Writing
		final RenderedOp op = JAI.create("ImageWrite", pbjImageWrite);
		final ImageWriter writer2 = (ImageWriter) op
				.getProperty(ImageWriteDescriptor.PROPERTY_NAME_IMAGE_WRITER);
		writer2.dispose();
		// ////////////////////////////////////////////////////////////////
		//
		// preparing to read again
		//
		// ////////////////////////////////////////////////////////////////
		final ParameterBlockJAI pbjImageReRead = new ParameterBlockJAI(
				"ImageRead");
		pbjImageReRead.setParameter("Input", outputFile);
		pbjImageReRead.setParameter("Reader", new JP2GDALKakaduImageReaderSpi()
				.createReaderInstance());
		final RenderedOp image2 = JAI.create("ImageRead", pbjImageReRead);
		if (TestData.isInteractiveTest())
			Viewer.visualize(image2, "Written Image");
		else
			image2.getTiles();
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());

	}
}
