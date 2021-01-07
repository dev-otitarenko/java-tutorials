package ua.maestro.lib.templates;

import ua.maestro.lib.templates.exceptions.ProcessTemplateException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import ua.maestro.lib.commons.Utils;

/**
 *
 * @author maestro
 */
public class ConvertXSL2HTML {
    public String process(String htmlContent) throws ProcessTemplateException {
        htmlContent = htmlContent.replace("&nbsp;" ," ");
        
        Document document = Jsoup.parse(htmlContent);
        document.outputSettings().syntax(Document.OutputSettings.Syntax.html);
        document.outputSettings().charset("UTF-8");

        Elements buttons = document.select("button");
        for(Element el : buttons) {
            el.remove();
        }
        
        int tabCnt = 0;
        Elements tables = document.select("[id=Process]");
        for(Element el : tables) {
            tabCnt++;

            el
                .addClass("table-template")
                .removeAttr("id");
            checkTableTemplate(el, tabCnt);
        }

        Elements fields = document.select("input");
        for(Element el : fields) {
            prepareInput(el);
        }

        return document.toString();
    }

    private void prepareInput(Element input) {
        Attributes newAttrs = new Attributes();
        String _id = input.attr("id"), _style = input.attr("style"), _class = input.attr("class"), _type = input.attr("type");
        if (_type.equalsIgnoreCase("hidden")) {
        //   if (!Utils.isEmpty(_style)) _style += ";display:none;" ; else _style = "display:none;";
            input.remove();
            return;
        }
        if (!Utils.isEmpty(_id)) newAttrs.put("p", _id);
        if (!Utils.isEmpty(_style)) { newAttrs.put("style", _style); }
        newAttrs.put("class", (!Utils.isEmpty(_class)) ? "field " +_class : "field");

        Element div = new Element(Tag.valueOf("div"), "", newAttrs);
        div.html(_id);
        input.replaceWith(div);        
    }
    
    private void prepareInputRn(Element input, int tabCnt, int cnt) {
        Attributes newAttrs = new Attributes();
        String _numtype = input.attr("numtype"),
               _id = input.attr("id"),
               _style = input.attr("style"),
               _class = input.attr("class"),
               _prefix = "";
        if (!Utils.isEmpty(_id))
            newAttrs.put("p", !_id.equalsIgnoreCase("sprownum") ? _id : "RN_TAB" + String.valueOf(tabCnt) + "_" + String.valueOf(cnt));
        else
            newAttrs.put("p", "RN_TAB" + String.valueOf(tabCnt) + "_" + String.valueOf(cnt));
        if (!Utils.isEmpty(_style)) { newAttrs.put("style", _style); }
        newAttrs.put("numtype", !Utils.isEmpty(_numtype) ? _numtype : "digit");
        newAttrs.put("class", (!Utils.isEmpty(_class)) ? "field field-rownum " +_class : "field field-rownum");
        if (!Utils.isEmpty(_prefix)) newAttrs.put("prefix", _prefix);

        Element div = new Element(Tag.valueOf("div"), "", newAttrs);
        input.replaceWith(div);        
    }
    
    private void prepareTblInputs(Element tr, int tabCnt) {
        int cntRn = 0;
        Elements spans = tr.select("span[id=spRownum]");
        for(Element input : spans) {
            cntRn++;
            prepareInputRn(input, tabCnt, cntRn);                   
        }
        
        Elements inputs = tr.select("input");
        for(Element input : inputs) {
            prepareInput(input);
        }
    }
    
    private void checkTableTemplate(Element tbl, int tabCnt) {
        Elements tmplTr = tbl.select("tr[id=StretchTable]");
        for(Element tr : tmplTr) {
            tr
              .removeAttr("id")
              .removeAttr("title")
              .removeAttr("rownum");
            tr
              .addClass("table-template-row");
            
            prepareTblInputs(tr, tabCnt);
        }
    }
}
