package com.mavriksc.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mavriksc.service.AnalysisService;
import com.mavriksc.service.MatchingService;
import com.mavriksc.types.Teams;
import com.mavriksc.types.TeamsConfirmed;

@Controller
public class IndexController {
	
	private static final Logger log = LoggerFactory.getLogger(IndexController.class);
	private static final String IMG_PROC_PATH = "./img-proc/";
	private static AnalysisService aService = new AnalysisService();
	
	@GetMapping("/")
    public String getIndex(Model model) {
        model.addAttribute("now", LocalDateTime.now());
        return "index";
    }
	
	@PostMapping("/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) throws IOException {
		//store the uploaded screenshot and redirect to the analysis page
		UUID guid = UUID.randomUUID();
        File upload = new File(IMG_PROC_PATH + guid);
        FileUtils.writeByteArrayToFile(upload, file.getBytes()); 
        return "redirect:/"+guid.toString();//return "redirect:/"+ guid or something 
    }
	
	@GetMapping("/{guid}")
    public String getAnalysis(@PathVariable String guid, Model model) {
        //do the analysis on the file and add items to the model 
		
		//MatchingService.scaleAndCheckAll(guid);
		Teams teams = MatchingService.sliceGetTeamsList(guid);
		TeamsConfirmed teamsConf = new TeamsConfirmed(teams);
		model.addAttribute("rankedList", aService.rankedList(teamsConf));
		model.addAttribute("guid", guid);
		model.addAttribute("teams", teams);
		model.addAttribute("imagePath", IMG_PROC_PATH + guid.toString());
        return "analysis";
    }
	
	@GetMapping("/img-proc/{guid}")
	public void getImage(@PathVariable() String guid,
							HttpServletResponse response) {
		try {
			// get your file as InputStream
			File image = new File(IMG_PROC_PATH + guid);
			InputStream is = new FileInputStream(image);
			// copy it to response's OutputStream
			response.setContentType(MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(image));			
			IOUtils.copy(is, response.getOutputStream());
			response.flushBuffer();
			is.close();
			//delete file
			image.delete();
		  
		} catch (IOException ex) {
		  log.info("Error writing file to output stream. Filename was '{}'", guid, ex);
		  throw new RuntimeException("IOError writing file to output stream");
		}	
	}
	
	@PostMapping("analysis/{guid}")
	public String confirmation(@ModelAttribute TeamsConfirmed teams,@PathVariable String guid){
		log.info(teams.getOurs().toString());
		
		return "redirect:/analysis/"+guid.toString();
	}
}
