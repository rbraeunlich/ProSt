package de.blogspot.wrongtracks.prost.fileupload;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;


/**
 * Servlet implementation class FileUploadServlet
 */
@WebServlet(name = "FileUploadServlet", urlPatterns = "/fileupload")
@MultipartConfig
public class FileUploadServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private final String dataDir = System
			.getProperty(UploadKonstanten.PROST_DATA_DIR_PROP);

	private File activitiDir = null;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public FileUploadServlet() {
		super();
	}

	@Override
	public void init() throws ServletException {
		super.init();
		activitiDir = new File(new File(dataDir),
				UploadKonstanten.PROST_DATA_DIR_NAME);
		activitiDir.mkdirs();
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/plain;charset=UTF-8");

		String ordnername = convertToPlatformEncoding(leseStringAusRequest(
				UploadKonstanten.REQUEST_PART_NUTZER, request));
		String dateiname = convertToPlatformEncoding(leseStringAusRequest(
				UploadKonstanten.REQUEST_PART_DATEINAME, request));

		File studentOrdner = new File(activitiDir, ordnername);
		studentOrdner.mkdirs();
		File pdf = new File(studentOrdner, dateiname);

		Part part = request.getPart(UploadKonstanten.REQUEST_PART_DATEI);

		InputStream inputStream = part.getInputStream();

		FileOutputStream fos = new FileOutputStream(pdf);
		byte[] data = new byte[256];
		while (inputStream.read(data) != -1) {
			fos.write(data);
		}
		inputStream.close();
		fos.close();
		response.setStatus(HttpServletResponse.SC_OK);
		PrintWriter writer = response.getWriter();
		String pdfPath = pdf.getAbsolutePath().trim();
		writer.append(new String(("file:///" + pdfPath).getBytes(Charset
				.forName("UTF-8")), Charset.forName("UTF-8")));
	}

	private String leseStringAusRequest(String zuLesenderPart,
			HttpServletRequest request) throws IOException, ServletException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(request.getPart(zuLesenderPart)
						.getInputStream(), Charset.forName("UTF-8")));
		String s = bufferedReader.readLine();
		bufferedReader.close();
		return s;
	}

	private String convertToPlatformEncoding(String utf8String) {
		return new String(utf8String.getBytes(Charset.defaultCharset()));
	}
}
