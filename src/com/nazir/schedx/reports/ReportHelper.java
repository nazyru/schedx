package com.nazir.schedx.reports;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
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
	
	@SuppressLint("NewApi")
	public static void genetareReport(Context context) {
		Document document = new Document();
		File dir;
		Font green = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, BaseColor.GREEN);
		
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO)
			dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
		else
			dir = Environment.getExternalStorageDirectory();
		
		File file = new File(dir, "schedules.pdf");
		
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
	}

	private static void addAssessmentTable(Document document, Context context) throws DocumentException {
		
		PdfPTable assessmentTable = new PdfPTable(new float[]{1f, 1f, 2f, 1f});
		assessmentTable.setWidthPercentage(100);
		
		AssessmentHelper helper = new AssessmentHelper(context);
		List<Assessment> assessments = helper.getAllAssessments();
		helper.disconnect();
		
		assessmentTable.addCell(new PdfPCell(new Phrase("Type")));
		assessmentTable.addCell(new PdfPCell(new Phrase("Course")));
		assessmentTable.addCell(new PdfPCell(new Phrase("Date/Time")));
		assessmentTable.addCell(new PdfPCell(new Phrase("Venue")));
		assessmentTable.setHeaderRows(1);
		
		for(Assessment assessment : assessments){
			assessmentTable.addCell(new PdfPCell(new Phrase(assessment.getAssessmentType().name())));
			assessmentTable.addCell(new PdfPCell(new Phrase(assessment.getCourse().getCourseCode())));
			assessmentTable.addCell(new PdfPCell(new Phrase(DateTimeHelper.getDisplayableDate(assessment.getDate()))));
			assessmentTable.addCell(new PdfPCell(new Phrase(assessment.getLocation())));
		}
		
		document.add(assessmentTable);
	}

	private static void addParagraph(Document doc, String text) throws DocumentException {
		doc.add(new Paragraph(text));
		
	}

	private static void addLectureTable(Document document, Context context) throws Exception {
		PdfPTable lectureTable = new PdfPTable(7);
		lectureTable.setWidthPercentage(100);
		
		LecturesHelper helper = new LecturesHelper(context);
		
		lectureTable.addCell(new PdfPCell(new Phrase("Monday")));
		lectureTable.addCell(new PdfPCell(new Phrase("Tuesday")));
		lectureTable.addCell(new PdfPCell(new Phrase("Wednesday")));
		lectureTable.addCell(new PdfPCell(new Phrase("Thursday")));
		lectureTable.addCell(new PdfPCell(new Phrase("Friday")));
		lectureTable.addCell(new PdfPCell(new Phrase("Saturday")));
		lectureTable.addCell(new PdfPCell(new Phrase("Sunday")));
		lectureTable.setHeaderRows(1);
		
		lectureTable.addCell(new PdfPCell(new Phrase("1.1")));
		lectureTable.addCell(new PdfPCell(new Phrase("1.2")));
		lectureTable.addCell(new PdfPCell(new Phrase("1.3")));
		lectureTable.addCell(new PdfPCell(new Phrase("1.4")));
		lectureTable.addCell(new PdfPCell(new Phrase("1.5")));
		lectureTable.addCell(new PdfPCell(new Phrase("1.6")));
		lectureTable.addCell(new PdfPCell(new Phrase("1.7")));
		
		/*List<Lecture> mondayLectures = helper.getLectures(Day.MONDAY);
		
		List<Lecture> wednesdayLectures = helper.getLectures(Day.WEDNESDAY);
		List<Lecture> thursdayLectures = helper.getLectures(Day.THURSDAY);
		List<Lecture> fridayLectures = helper.getLectures(Day.FRIDAY);
		List<Lecture> saturdayLectures = helper.getLectures(Day.SATURDAY);
		List<Lecture> sundayLectures = helper.getLectures(Day.SUNDAY);
		*/
		
		List<Lecture> tuesdayLectures = helper.getLectures(Day.TUESDAY);
		
		
		for(Lecture lecture: tuesdayLectures){
			lectureTable.addCell(getLectureEntry(lecture));
		}
		
		document.add(lectureTable);
		
		helper.disconnect();
	}

	private static PdfPCell getLectureEntry(Lecture lecture) {
		
		String startingTime = DateTimeHelper.getTimeToString(lecture.getStartTime());
		String endTime = DateTimeHelper.getTimeToString(lecture.getEndTime());
		
		PdfPCell cell = new PdfPCell();
		Paragraph courseCodePara = new Paragraph(lecture.getCourse().getCourseCode());
		Paragraph timePara = new Paragraph(startingTime + " - "+ endTime);
		Paragraph venuePara = new Paragraph(lecture.getVenue());
		
		courseCodePara.setAlignment(Element.ALIGN_CENTER);
		timePara.setAlignment(Element.ALIGN_JUSTIFIED);
		venuePara.setAlignment(Element.ALIGN_CENTER);
		
		cell.addElement(courseCodePara);
		cell.addElement(timePara);
		cell.addElement(venuePara);
		
		return cell;
	}

	private static void addCourseTable(Document doc, Context context) throws DocumentException {
		PdfPTable courseTable = new PdfPTable(new float[]{1f, 2f, 1f});
		courseTable.setWidthPercentage(100);
		
		courseTable.addCell(new PdfPCell(new Phrase("Course Code")));
		courseTable.addCell(new PdfPCell(new Phrase("Course Title")));
		courseTable.addCell(new PdfPCell(new Phrase("Course Unit")));
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
	
	private static void addMetaData(Document doc) {
		doc.addTitle("SchedX App Report");
		doc.addAuthor("nazir bunu <nazyru@gmail.com>");
		doc.addCreationDate();
		doc.addSubject("Lecture, Assessment And TODO Reports Generation");
		
	}

	private static void addEmptyLine(Document document, int num) throws DocumentException{
		for(int i=0; i<num; i++)
			document.add(new Paragraph(" "));
	}
}
