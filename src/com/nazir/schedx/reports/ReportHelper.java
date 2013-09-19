package com.nazir.schedx.reports;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.nazir.schedx.model.Assessment;
import com.nazir.schedx.model.Course;
import com.nazir.schedx.model.Lecture;
import com.nazir.schedx.persist.AssessmentHelper;
import com.nazir.schedx.persist.CoursesHelper;
import com.nazir.schedx.persist.LecturesHelper;
import com.nazir.schedx.persist.MySqliteOpenHelper.Courses;
import com.nazir.schedx.types.Day;
import com.nazir.schedx.util.DateTimeHelper;

public class ReportHelper {
	private Context context;
	private HashMap<Day, List<Lecture>> lectures = new HashMap<Day, List<Lecture>>();
	private HashMap<Day, Iterator<Lecture>> iterators = new HashMap<Day, Iterator<Lecture>>();
	private BaseColor lightGreen = new BaseColor(0x99, 0xcc, 0x00);
	private BaseColor darkWhite = new BaseColor(0xf2, 0xf2, 0xf2);
	private Font darkWhiteFont = new Font(Font.FontFamily.COURIER, 16, Font.BOLD, darkWhite);
	
	public ReportHelper(Context context) {
		this.context = context;
	}
	
	public void doReport(){
		MyAsyncTask task = new MyAsyncTask();
		task.execute();
	}

	@SuppressLint({ "NewApi", "SimpleDateFormat" })
	public File genetareReport() {
		Document document = new Document(PageSize.A4.rotate());	 //landscape mode
		
		File dir;
		
		Font green = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD, lightGreen);
		
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO)
			dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
		else
			dir = Environment.getExternalStorageDirectory();
		
		String dateAppend = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
		String filename = "schedX"+dateAppend+".pdf";
		File file = new File(dir,  filename);
		
		try{
			PdfWriter.getInstance(document, new FileOutputStream(file));
			document.open();
			addMetaData(document);
			
			Paragraph header = new Paragraph("SchedX Scheduler App", green);
			header.setAlignment(Paragraph.ALIGN_CENTER);
			document.add(header);
			
			InputStream is = context.getResources().getAssets().open("ic_launcher.png");
			byte data[] = new byte[is.available()];
			is.read(data);
				
			Image image = Image.getInstance(data);
			image.setAlignment(Image.ALIGN_LEFT);
			document.add(image);
			
			addEmptyLine(document, 1);
			addParagraph(document, "SchedX Lecture Schedules");
			
			addEmptyLine(document, 1);
			addLectureTable(document, context);
			addEmptyLine(document, 1);
			
			addParagraph(document, "Assessments");
			addEmptyLine(document, 1);
			addAssessmentTable(document, context);
			addEmptyLine(document, 1);
			
			addParagraph(document, "Courses");
			addEmptyLine(document, 1);
			addCourseTable(document, context);		
			
			document.close();
		
		}catch(Exception e){
			Log.e("--Report Generation--", e.getMessage());
		}
		return file;
	}

	private void addAssessmentTable(Document document, Context context) throws DocumentException {
		
		PdfPTable assessmentTable = new PdfPTable(new float[]{1f, 1f, 2f, 1f});
		assessmentTable.setWidthPercentage(100);
		
		AssessmentHelper helper = new AssessmentHelper(context);
		List<Assessment> assessments = helper.getAllAssessments();
		helper.disconnect();
		
		PdfPCell typeHeader = new PdfPCell(new Phrase("Type", darkWhiteFont));
		PdfPCell courseHeader = new PdfPCell(new Phrase("Course", darkWhiteFont));
		PdfPCell dateTimeHeader = new PdfPCell(new Phrase("Date/Time", darkWhiteFont));
		PdfPCell venueHeader = new PdfPCell(new Phrase("Venue", darkWhiteFont));
		
		typeHeader.setBackgroundColor(lightGreen);
		courseHeader.setBackgroundColor(lightGreen);
		dateTimeHeader.setBackgroundColor(lightGreen);
		venueHeader.setBackgroundColor(lightGreen);
		
		typeHeader.setBorder(0);
		courseHeader.setBorder(0);
		dateTimeHeader.setBorder(0);
		venueHeader.setBorder(0);
		
		assessmentTable.addCell(typeHeader);
		assessmentTable.addCell(courseHeader);
		assessmentTable.addCell(dateTimeHeader);
		assessmentTable.addCell(venueHeader);
		assessmentTable.setHeaderRows(1);
		
		for(Assessment assessment : assessments){
			assessmentTable.addCell(new PdfPCell(new Phrase(assessment.getAssessmentType().name())));
			assessmentTable.addCell(new PdfPCell(new Phrase(assessment.getCourse().getCourseCode())));
			assessmentTable.addCell(new PdfPCell(new Phrase(DateTimeHelper.getDisplayableDate(assessment.getDate()))));
			assessmentTable.addCell(new PdfPCell(new Phrase(assessment.getLocation())));
		}
		
		document.add(assessmentTable);
	}

	private void addParagraph(Document doc, String text) throws DocumentException {
		doc.add(new Paragraph(text));
		
	}

	private void addLectureTable(Document document, Context context) throws Exception {
		PdfPTable lectureTable = new PdfPTable(14);
		lectureTable.setWidthPercentage(100);
		
		LecturesHelper helper = new LecturesHelper(context);
		PdfPCell headerCell;
		
		for(Day day: Day.values()){
			if(day == Day.CHOOSE)
				continue;
			
			headerCell = new PdfPCell(new Phrase(day.toString(), darkWhiteFont));
			headerCell.setColspan(2);
			headerCell.setBackgroundColor(lightGreen);
			headerCell.setBorder(0);
			lectureTable.addCell(headerCell);
		}
		
		lectureTable.setHeaderRows(1);
		
		for(Day day: Day.values()){
			if(day == Day.CHOOSE)
				continue;
			lectures.put(day, helper.getLectures(day));
			iterators.put(day, lectures.get(day).iterator());
		}
		
		for(List<PdfPCell> row: getTableContent()){
			for(PdfPCell cell: row){
				lectureTable.addCell(cell);
			}
		}
		
		document.add(lectureTable);
		
		helper.disconnect();
	}

	private PdfPCell getLectureEntry(Lecture lecture) {
		
		String startingTime = DateTimeHelper.getTimeToString(lecture.getStartTime());
		String endTime = DateTimeHelper.getTimeToString(lecture.getEndTime());
		
		PdfPCell cell = new PdfPCell();
		cell.setColspan(2);
		
		Paragraph courseCodePara = new Paragraph(lecture.getCourse().getCourseCode());
		Paragraph timePara = new Paragraph(startingTime + " - "+ endTime);
		Paragraph venuePara = new Paragraph(lecture.getVenue());
		
		courseCodePara.setAlignment(Element.ALIGN_CENTER);
		timePara.setAlignment(Element.ALIGN_CENTER);
		
		venuePara.setAlignment(Element.ALIGN_CENTER);
		
		cell.addElement(courseCodePara);
		cell.addElement(timePara);
		cell.addElement(venuePara);
		
		return cell;
	}
	
	private PdfPCell getEmptyEntry(){
		Paragraph emptyPara = new Paragraph("---");
		emptyPara.setAlignment(Element.ALIGN_CENTER);
		PdfPCell emptyCell = new PdfPCell(emptyPara);
		emptyCell.setColspan(2);
		return emptyCell;
	} 
	
	private List<PdfPCell> getNextRow(){
		List<PdfPCell> cells = new ArrayList<PdfPCell>();
			
		while(hasNext()){
			for(Day day: Day.values()){
				if(day == Day.CHOOSE)
					continue;
				
				if(iterators.get(day).hasNext()){
					Lecture lecture = iterators.get(day).next();
					cells.add(getLectureEntry(lecture));
					
				}else{
					cells.add(getEmptyEntry());
				}
			}
		}
		return cells;
	}
	
	private boolean hasNext() {
		boolean status = false;
		
		for(Day day: Day.values()){
			if(day == Day.CHOOSE)
				continue;
			
			if(iterators.get(day).hasNext() == true)
				status = true;
		}
		return status;
	}

	private List<List<PdfPCell>> getTableContent(){
		List<List<PdfPCell>> contents = new ArrayList<List<PdfPCell>>();
		List<PdfPCell> nextRow = new ArrayList<PdfPCell>();
		
		do{
			nextRow = getNextRow();
			if(nextRow.isEmpty())
				break;
			
			contents.add(nextRow);
		}
		while(true);
		
		return contents;
	}

	private void addCourseTable(Document doc, Context context) throws DocumentException {
		PdfPTable courseTable = new PdfPTable(new float[]{1f, 2f, 1f});
		courseTable.setWidthPercentage(100);
		
		PdfPCell courseCodeHeader = new PdfPCell(new Phrase("Course Code", darkWhiteFont));
		PdfPCell courseTitleheader = new PdfPCell(new Phrase("Course Title", darkWhiteFont));
		PdfPCell courseunitHeader = new PdfPCell(new Phrase("Course Unit", darkWhiteFont));
		
		courseCodeHeader.setBackgroundColor(lightGreen);
		courseTitleheader.setBackgroundColor(lightGreen);
		courseunitHeader.setBackgroundColor(lightGreen);
		
		courseCodeHeader.setBorder(0);
		courseTitleheader.setBorder(0);
		courseunitHeader.setBorder(0);
		
		courseTable.addCell(courseCodeHeader);
		courseTable.addCell(courseTitleheader);
		courseTable.addCell(courseunitHeader);
		courseTable.setHeaderRows(1);
		
		CoursesHelper helper = new CoursesHelper(context);
		List<Course> courses = helper.getAllCourses();
		helper.disconnect();
		
		for(Course course: courses){
			courseTable.addCell(new PdfPCell(new Phrase(course.getCourseCode())));
			courseTable.addCell(new PdfPCell(new Phrase(course.getCourseTitle())));
			courseTable.addCell(new PdfPCell(new Phrase(course.getCourseUnit()+ "")));
		}
		
		doc.add(courseTable);
	}
	
	private void addMetaData(Document doc) {
		doc.addTitle("SchedX App Report");
		doc.addAuthor("nazir bunu <nazyru@gmail.com>");
		doc.addCreationDate();
		doc.addSubject("Lecture, Assessment And TODO Reports Generation");
		
	}

	private void addEmptyLine(Document document, int num) throws DocumentException{
		for(int i=0; i<num; i++)
			document.add(new Paragraph(" "));
	}
	
	private class MyAsyncTask extends AsyncTask<Void, Void, File>{

		@Override
		protected File doInBackground(Void... params) {
			
			return genetareReport();
		}

		@Override
		protected void onPostExecute(File result) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setType("application/pdf");
			intent.setData(Uri.fromFile(result));
			context.startActivity(Intent.createChooser(intent, "Share Schedules Via..."));
			
		}
	}
}
