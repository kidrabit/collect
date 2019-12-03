package com.pcn.manager.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pcn.manager.command.Shutter;
import com.pcn.manager.service.WebService;

/**
 * @class WebController
 * 	com.pcn.manager.controller
 * @section 클래스작성정보
 *    |    항  목        	|       	내  용       			|
 *    | :--------: 	| -----------------------------	|
 *    | Company 	| PCN
 *    | Author 		| rnd
 *    | Date 		| 2019. 8. 20.
 *    | 작업자 		| rnd, Others...
 * @section 상세설명
 * - 클래스의 업무내용에 대해 기술...
*/
//설정파일 외부로 뺄때 주석 처리 해야함
//
@Configuration
@PropertySource("classpath:logstash.properties")
//
@Controller
public class WebController {

    @Value("${logstash.os}")
    private String logstash_os;

    @Value("${logstash.dir.path}")
    private String logstash_dir_path;

    @Value("${logstash.port}")
    private String logstash_port;
    
    @Value("${conf.file.name}")
    private String conf_file_name;
    
    /*
    @Value("${es.port}")
    private String es_port;
    */
    @Autowired
    public Shutter shutter;

    @Autowired
    public WebService webService;
    
    //404
    /*
    @RequestMapping(value = "/manager/**")
    public void viewNotMappingRequest(HttpServletRequest req, HttpServletResponse res,@RequestHeader HttpHeaders headers) throws NoHandlerFoundException {
    	throw new NoHandlerFoundException("URL not found exception", req.getRequestURL().toString(), headers);
    }
    */

    @RequestMapping(value = "/manager/start.ajax", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> start(Model model) throws Exception {
    	shutter = new Shutter();
    	Map<String, String> msgMap = shutter.engineStart(logstash_os, logstash_dir_path, logstash_port);
        return msgMap;
    }

    @RequestMapping(value = "/manager/shutdown.ajax", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> shutDown(Model model)  throws Exception {
    	shutter = new Shutter();
    	Map<String, String> msgMap = shutter.engineDown(logstash_os, logstash_port);
        return msgMap;
    }

    @RequestMapping("/manager/loadEdit")
    @ResponseBody
    public Map<String, String> loadEdit(Model model, @RequestParam Map<String, String> paramMap) {
        paramMap.put("contents", webService.loadEdit(model, paramMap));
        return paramMap;
    }

    @RequestMapping(value = "/manager/saveEdit", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> saveEdit(Model model, @RequestParam Map<String, String> paramMap) {
        webService.saveEdit(model, paramMap);
        return paramMap;
    }

    @RequestMapping("/manager/loadEditDt.ajax")
    @ResponseBody
    public Map<String, String> loadEditDt(Model model, @RequestParam Map<String, String> paramMap) {
        paramMap.put("contents", webService.loadEditDt(model, paramMap));
        return paramMap;
    }

    @RequestMapping(value = "/manager/saveEditDt.ajax", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> saveEditDt(Model model, @RequestParam Map<String, String> paramMap) {
        webService.saveEditDt(model, paramMap);
        return paramMap;
    }

    @RequestMapping(value = "/manager/index")
    public String boot(Model model) {
        return "index";
    }

    @RequestMapping(value = "/manager/charts")
    public String charts(Model model) {
        model.addAttribute("menu", "charts");
        return "charts";
    }

    @RequestMapping(value = "/manager/write")
    public String wrtiePlugins(Model model) {
        List<String> fileNmList = webService.getConfFileList();
        model.addAttribute("fileNmList", fileNmList);
        model.addAttribute("menu", "write");
        return "writePlug";
    }

    @RequestMapping(value = "/manager/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("menu", "dashboard");
        return "dashboard";
    }

    @RequestMapping(value = "/manager/flowChart")
    public String flowChart(Model model, @RequestParam Map<String, String> paramMap) {
        paramMap.put("fileName", conf_file_name);
        paramMap.put("confSection", "input");
        model.addAttribute("inputList", webService.sectionItemList(model, paramMap));
        paramMap.put("confSection", "output");
        model.addAttribute("outputList", webService.sectionItemList(model, paramMap));
        model.addAttribute("menu", "flowChart");
        return "flowChart";
    }

    @RequestMapping(value = "/popup/write")
    public String popupWrtiePlugins(Model model) {
        List<String> fileNmList = webService.getConfFileList();
        model.addAttribute("fileNmList", fileNmList);
        return "popupWritePlug";
    }

    @RequestMapping(value = "/popup/writeView")
    public String popupWrtieView(Model model, @RequestParam Map<String, String> paramMap) {
    	paramMap.put("fileName", conf_file_name);
        model.addAttribute("contents", webService.loadEdit(model, paramMap));
        return "popupWritePlugView";
    }

    @RequestMapping(value = "/popup/addConfFile")
    public String popupAddConfFile(Model model) {
        return "popupAddConfFile";
    }

    @RequestMapping(value = "/popup/addConfFileSave", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> addConfFileSave(Model model, @RequestParam Map<String, String> paramMap) {
        webService.addConfFile(model, paramMap);
        return paramMap;
    }

    @RequestMapping(value = "/manager/writeUi")
    public String wrtieUiPlug(Model model, @RequestParam Map<String, String> paramMap) {
        model.addAttribute("menu", "writeUi");
        model.addAttribute("pageMove", paramMap.get("pageMove"));//FlowChar메뉴에서 Plugin Manager페이지 이동시 사용
        return "writeUiPlug";
    }

    @RequestMapping(value = "/form/selectForm.ajax", method = RequestMethod.POST)
    public String selectForm(Model model, @RequestParam Map<String, String> paramMap) {
        webService.getSelectList(model, paramMap);
        return "uiForms/selectForm";
    }

    @RequestMapping(value = "/form/writeForm.ajax", method = RequestMethod.POST)
    public String inputForm(Model model, @RequestParam Map<String, String> paramMap) {
    	paramMap.put("fileName", conf_file_name);
        model.addAttribute("confSection", paramMap.get("confSection"));
        model.addAttribute("formJson", webService.getWriteFormJson(paramMap.get("confSection")));
        model.addAttribute("dataJsonArray", webService.getConfJsonObject(model, paramMap));
        return "uiForms/writeForm";
    }

    @RequestMapping(value = "/manager/writeUiSave.ajax", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> writeUiSave(Model model, @RequestParam Map<String, String> paramMap, HttpServletRequest request) {
    	paramMap.put("fileName", conf_file_name);
        String[] tabItemNm = request.getParameterValues("tabItemNm");
        webService.writeUiSave(model, paramMap, tabItemNm, request);
        return paramMap;
    }
}
