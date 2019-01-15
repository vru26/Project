package com.lnt.mvc.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.lnt.mvc.dao.DocumentDAOImpl;
import com.lnt.mvc.model.Document;
import com.lnt.mvc.model.EMI;
import com.lnt.mvc.service.IEMIService;
import com.lnt.utils.LoginUtility;

@Controller
// @RequestMapping("redirect")
public class MainController {
	@Autowired
	IEMIService emiService;
	@Autowired
	SessionFactory sf;

	@RequestMapping(value = "login", method = RequestMethod.POST)
	public String login(HttpServletRequest request) {

		/*
		 * AbstractApplicationContext ctx = new
		 * ClassPathXmlApplicationContext("servlet-context.xml");
		 * ctx.registerShutdownHook();
		 */
		String uname = request.getParameter("username");
		String pwd = request.getParameter("password");
		LoginUtility util = new LoginUtility(request.getParameter("username"), request.getParameter("password"));
		/*
		 * LoginUtility util = (LoginUtility) ctx.getBean(uname, pwd,
		 * LoginUtility.class);
		 */
		System.out.println(util.isValidUser());
		System.out.println(request.getParameter("username") + "vxd " + request.getParameter("password"));
		return "successful-login";
	}

	@RequestMapping("/calculate")
	public String displayCalculator(Model model) {
		model.addAttribute("calculatorValues", new EMI());
		return "no-login-emi-calculator";
	}

	@RequestMapping("/calculator")
	public String calculate(@ModelAttribute("calculatorValues") EMI e, BindingResult result, ModelMap model) {
		double emi = emiService.calculateEMI(e);
		double totalAmtPayable = emiService.calculateTotalAmtPayable(emi, e.getTenure());
		double totalInterest = emiService.calculateTotalInterest(e.getLoanAmount(), totalAmtPayable);
		model.addAttribute("emi", Math.rint(emi));
		model.addAttribute("TotalInterest", Math.rint(totalInterest));
		model.addAttribute("AmtPayable", Math.rint(totalAmtPayable));
		return "successful-login";
	}

	@RequestMapping("emi")
	public String display(Model model) {
		model.addAttribute("calculatorValues", new EMI());
		return "login-emi-calculator";
	}

	@Autowired
	private DocumentDAOImpl documentDao;

	@RequestMapping("/index")
	public String index(Map<String, Object> map) {
		try {
			map.put("document", new Document());
			map.put("documentList", documentDao.list());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "documents";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(@ModelAttribute("document") Document document, @RequestParam("file") MultipartFile file) {

		System.out.println("Name:" + document.getName());
		System.out.println("Desc:" + document.getDescription());
		System.out.println("File:" + file.getName());
		System.out.println("ContentType:" + file.getContentType());

		try {
			byte[] b = file.getBytes();
			Session s = sf.openSession();
			Blob blob = Hibernate.getLobCreator(s).createBlob(b);
			// Blob blob = Hibernate.createBlob(file.getInputStream());
			document.setFilename(file.getOriginalFilename());
			document.setContentType(file.getContentType());
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			System.out.println("in");
			documentDao.save(document);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "redirect:/index.html";
	}

	// @RequestMapping("/download")
	@RequestMapping("/download/{documentId}")
	public String download(@PathVariable("documentId") Integer documentId, HttpServletResponse response) {

		Document doc = documentDao.get(documentId);
		try {
			response.setHeader("Content-Disposition", "inline;filename=\"" + doc.getFilename() + "\"");
			System.out.println("out");
			OutputStream out = response.getOutputStream();
			response.setContentType(doc.getContentType());
			IOUtils.copy(doc.getContent().getBinaryStream(), out);
			out.flush();
			out.close();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	@RequestMapping("/remove/{documentId}")
	public String remove(@PathVariable("documentId") Integer documentId) {

		documentDao.remove(documentId);

		return "redirect:/index.html";
	}
}
