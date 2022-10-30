package com.coservice.service;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coservice.entity.CitizenDetailsEntity;
import com.coservice.entity.CoTrigger;
import com.coservice.entity.EligibilityEntity;
import com.coservice.repository.CitizenDetailsRepository;
import com.coservice.repository.CoTriggerRepository;
import com.coservice.repository.EligibilityRepository;
import com.coservice.utils.EmailsUtils;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

@Service
public class CoTriggerServiceImpl implements CoTriggerService {
	@Autowired
	private CoTriggerRepository corepo;
	
	@Autowired
	private EligibilityRepository eligrepo;
	
	@Autowired
	private EmailsUtils eutils;
	
	@Autowired
	private CitizenDetailsRepository crepo;
	

	@Override
	public String cotriggerOperations() throws Exception {
		List<CoTrigger> coentities = corepo.findAll();
		
		if(!coentities.isEmpty()) {
			for(CoTrigger coentity : coentities) {
				if(coentity.getTriggerStatus().equalsIgnoreCase("Pending")) { 
					byte[] createPdf = createPdf();
				   coentity.setCoPdf(createPdf());
				   coentity.setTriggerStatus("completed");
				   corepo.save(coentity);
			}
				
		}
		
			return "mail Send";
	}
	
		return null;
		
	}
	
	   private byte[] createPdf() throws Exception {
		   String holderName = null;
		   String planName = null;
		   String planStatus = null;
		   String email = null;
		   
		   
		   Document document = new Document(PageSize.A4);
		   ByteArrayOutputStream baos = new ByteArrayOutputStream();
		   PdfWriter pdfWriter = PdfWriter.getInstance(document, baos);
	         
		        document.open();
		        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		        font.setSize(18);
		        font.setColor(Color.BLUE);
		         
		        Paragraph p = new Paragraph("Citizen Notice", font);
		        p.setAlignment(Paragraph.ALIGN_CENTER);
		         
		        document.add(p);
		         
		        PdfPTable table = new PdfPTable(7);
		        table.setWidthPercentage(100f);
		        table.setWidths(new float[] {1.5f, 3.5f, 3.0f, 3.0f, 1.5f, 1.5f, 3.0f});
		        table.setSpacingBefore(10);
		        
		        PdfPCell cell = new PdfPCell();
		        cell.setBackgroundColor(Color.BLUE);
		        cell.setPadding(5);
		         
		        Font font1 = FontFactory.getFont(FontFactory.HELVETICA);
		        font1.setColor(Color.WHITE);
		         
		        cell.setPhrase(new Phrase("Citizen Name", font1));
		         
		        table.addCell(cell);
		        
		         
		        cell.setPhrase(new Phrase("PlanName", font1));
		        table.addCell(cell);
		         
		        cell.setPhrase(new Phrase("PlanStatus", font1));
		        table.addCell(cell);
		         
		        cell.setPhrase(new Phrase("PlanStartDate", font1));
		        table.addCell(cell);
		         
		        cell.setPhrase(new Phrase("PlanEndDate", font1));
		        table.addCell(cell);
		        
		        cell.setPhrase(new Phrase("BenefieAmount", font1));
		        table.addCell(cell);
		        
		        cell.setPhrase(new Phrase("Denial Reason", font1));
		        table.addCell(cell);
		        
		        List<EligibilityEntity> eligentities = eligrepo.findAll();
		        if(!eligentities.isEmpty()) {
		        for(EligibilityEntity eentity : eligentities) {
		        holderName = eentity.getHolderName();
		        planName = eentity.getPlanName();
		        planStatus = eentity.getPlanStatus();
		        
		        table.addCell(holderName);
		        table.addCell(planName);
		        table.addCell(planStatus);
		        table.addCell(eentity.getPlanStartDate());
		        table.addCell(eentity.getPlanEndDate());
		        table.addCell(eentity.getBenefitAmnt());
		        table.addCell(eentity.getDenialReason());
		        }
		        }
		        
		        document.add(table);
		        document.close();
		        
		        pdfWriter.flush();
		        
		        
		        
		         byte[] byteArray = baos.toByteArray();
		         String file = "notice.pdf";
		         FileOutputStream fos = new FileOutputStream(file);
		         fos.write(byteArray);
		         
		         List<CitizenDetailsEntity> centities = crepo.findAll();
		         if(!centities.isEmpty()) {
		         for(CitizenDetailsEntity centity : centities) {
		        	email = centity.getEmail(); 
		         }
		         }
		         
		         //send email
		         String file1 = "citizen-email.txt";
		         String subject = "Plan Application";
		         String mailBody = getMailBody(holderName, planName, planStatus, file1);
		         eutils.sendEmail(email, subject, mailBody);
		         return byteArray;
	   }
	   
	   private String getMailBody(String holderName, String planName, String planStatus, String filename){
			String line = null;
			try(FileReader reader = new FileReader(filename);
				  BufferedReader bufferedreader1 = new BufferedReader(reader)) 
			{
				
				StringBuilder builder = new StringBuilder();
				String readLine = bufferedreader1.readLine();
				while (readLine != null) {
					builder.append(readLine);
					readLine = bufferedreader1.readLine();
				}
				bufferedreader1.close();
				line = builder.toString();
				line = line.replace("{FULLNAME}", holderName);
				line = line.replace("{PLANNAME}", planName);
				line = line.replace("{PLANSTATUS}", planStatus);

			} catch (Exception e) {
				e.printStackTrace();
			}

			return line;
		}
	   

}
