package ua.maestro.lib.providers.stat.xml;

import ua.maestro.lib.commons.Utils;
import ua.maestro.lib.commons.utils.RegexpUtl;
import ua.maestro.lib.xmlcore.provider.interfaces.IXmlParam;
import ua.maestro.lib.xmlcore.domains.PZDocInput;
import ua.maestro.lib.xmlcore.domains.PZPayerData;
import ua.maestro.lib.xmlcore.domains.PZDocumentData;
import ua.maestro.lib.xmlcore.utils.PzUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author maestro, 2015
 */
public class XMLParam implements IXmlParam {
    private PZDocumentData _doc;
    private PZPayerData _pay;
    private List<PZDocInput> _docVars;
    private long _cnt4Curdate;

    public XMLParam(PZDocumentData doc, PZPayerData pay, List<PZDocInput> docVars, long cnt4Curdate) {
        this._doc = doc;
        this._pay = pay;
        this._docVars = docVars;
        this._cnt4Curdate = cnt4Curdate;
    }

    public PZDocumentData getDoc() { return _doc; }

    public PZPayerData getPay() { return _pay; }

        /*
            Получаем значение по умолчанию для реквизита отчета:
            1. pay - Контекст предприятия
            #{pay.tin} - Код плательщика
            #{pay.tin_ur} - Код юр.лица
            #{pay.tin_fiz} - Код физ.лица
            #{pay.name} -Наименование предприятия (ФИО физ.лица)
            #{pay.name_ur} - Наименование юр.лица
            #{pay.name_fiz} - ФИО физ.лица
            #{pay.kved} - Код ВЕД
            #{pay.kvedName} - Наименование КВЕДа
            #{pay.opf} - ОПФ
            #{pay.opfName} - Наименование ОПФ
            #{pay.address} - Адресн
            #{pay.zip} - Почтовый код
            #{pay.tel} - Телефон
            #{pay.fax} - Факс
            #{pay.email} - EMAIL
            #{pay.fullStiName} - 
            #{pay.stiTin}/#{pay.tinSti} - Код ЭДРПОУ налоговой
            #{pay.sti} - Код налоговой
            #{pay.stiName} - Наименование налоговой
            #{pay.koatuu} - Код КОАТУУ
            #{pay.koatuuName} - Наименование КОАТУУ
            #{pay.coogu} - Код органу державного управління за СПОДУ
            #{pay.cooguName} - орган державного управління
            #{pay.okonx} - Код галузі(виду діяльності) за ЗКГНГ
            #{pay.okonxName} - Найменування галузі(виду діяльності) за ЗКГНГ
            #{pay.kfv} - Код форми власності за КФВ
            #{pay.kfvName} - Найменування форми власності за КФВ
            #{pay.dirInn} - ИНН директора
            #{pay.buhInn} - ИНН бухгалтера
            #{pay.dirPib} - ФИО директора
            #{pay.buhPib} - ФИО бухгалтера
            #{pay.dirTel} - Телефон директора
            #{pay.buhTel} - Телефон бухгалтера
            #{pay.regName} - Наименование области
            #{pay.vatId} - ИНН предприятия
            #{pay.vatNompp} - № свидетельства НДС      

            2. Контекст параметров документа
            #{prm.perYear} - Отчетный год
            #{prm.perMonth} - Отчетный месяц
            #{prm.perType} - Отчетный тип
            #{prm.perTypeNm} - Название отчетноготипа
            #{prm.perMonthNm} - Название отчетного месяца
            #{prm.perPeriodNm} - Название отчетного периода (июнь 2015)
            #{prm.lastday} - Получить последний день отчетного месяца
            #{prm.currdate} - Текущая дата
            #{prm.currtime} - Текущее время
        */
    public String getDefaultValue (final PZDocInput inp) {
        String ret = "";
        if (inp != null) {
            String cterrit = this._pay.getCTerrit();
                // Дата заполнения
            if (inp.getField().equalsIgnoreCase("HFILL") && inp.getType().equalsIgnoreCase("DGDATE"))
                    ret = PzUtils.getCurrDateAsXmlStr();
            if (inp.getField().equalsIgnoreCase("HTIME") && inp.getType().equalsIgnoreCase("xs:time"))
                    ret = PzUtils.getCurrTimeAsXmlStr();
                // Отчетный год
            if (inp.getField().equalsIgnoreCase("HZY") && inp.getType().equalsIgnoreCase("DGYEAR")) 
                    //if (xmlPrm.getDoc().getDocStan() == 1 || xmlPrm.getDoc().getDocStan() == 2)
                    ret = String.valueOf(_doc.getPeriodYear());
                // Отчетный период за какой подаются правки
            if (inp.getField().equalsIgnoreCase("HZYP") && inp.getType().equalsIgnoreCase("DGYEAR") && _doc.getCDocStan() == 3) 
                    ret = String.valueOf(_doc.getPeriodYear());
                // Отчетный месяц
            if (inp.getField().equalsIgnoreCase("HZM") && inp.getType().equalsIgnoreCase("DGMONTH")) 
                    // if (xmlPrm.getDoc().getDocStan() == 1 || xmlPrm.getDoc().getDocStan() == 2)
                    if (_doc.getPeriodType() == 1) ret = String.valueOf(_doc.getPeriodMonth());

                // Отченый месяц за какой подается уточненка
            if (inp.getField().equalsIgnoreCase("HZMP") && inp.getType().equalsIgnoreCase("DGMONTH") && _doc.getCDocStan() == 3) 
                if (_doc.getPeriodType() == 1) ret = String.valueOf(_doc.getPeriodMonth());

                // Отченый квартал
            if (inp.getField().equalsIgnoreCase("HZKV") && inp.getType().equalsIgnoreCase("DGKV"))
                     //if (xmlPrm.getDoc().getDocStan() == 1) {
                    if (_doc.getPeriodType() >= 2) ret = String.valueOf(_doc.getPeriodMonth()/3);
                // Отчетный квартал, за который подается уточненка
            if (inp.getField().equalsIgnoreCase("HZKVP") && inp.getType().equalsIgnoreCase("DGKV") && _doc.getCDocStan() == 3)
                    if (_doc.getPeriodType() >= 2) ret = String.valueOf(_doc.getPeriodMonth()/3);                

            //SetValueForId("HZ","x",currentForm,"fieldIsEmpty(nextEl)","id");
            //SetValueForId("HCOPY","x",currentForm,"fieldIsEmpty(nextEl)","id");
            //SetValueForId("HNPDV",GetMainParameter(setDoc,"IPN"),currentForm,"fieldIsEmpty(nextEl)","id");
            if (inp.getField().equalsIgnoreCase("H1KV") && _doc.getPeriodMonth() == 3 && _doc.getPeriodType() == 2 && inp.getType().equalsIgnoreCase("dgchk"))
                    ret = String.valueOf("1");
            if (inp.getField().equalsIgnoreCase("HHY")&& _doc.getPeriodType() == 3 && inp.getType().equalsIgnoreCase("dgchk"))
                    ret = String.valueOf("1");
            if (inp.getField().equalsIgnoreCase("H3KV")&& _doc.getPeriodMonth() == 9 && _doc.getPeriodType() == 2 && inp.getType().equalsIgnoreCase("dgchk"))
                    ret = String.valueOf("1");
            if (inp.getField().equalsIgnoreCase("HY")&& _doc.getPeriodType() == 5) {
                if (inp.getType().equalsIgnoreCase("dgchk"))
                    ret = String.valueOf("1");
                else if (inp.getType().equalsIgnoreCase("dgyear"))
                    ret = String.valueOf(_doc.getPeriodYear());
            }
            if (inp.getField().equalsIgnoreCase("HJ")&& _pay.getFacemode() == 1 && inp.getType().equalsIgnoreCase("dgchk"))
                    ret = String.valueOf("1");
            if (inp.getField().equalsIgnoreCase("HF")&& _pay.getFacemode() == 1 && inp.getType().equalsIgnoreCase("dgchk"))
                    ret = String.valueOf("1");
            if (inp.getField().equalsIgnoreCase("HKP")&& _pay.getFacemode() == 1 && inp.getType().equalsIgnoreCase("dgchk"))
                    ret = String.valueOf("1");
            if (inp.getField().equalsIgnoreCase("HNPDV")&& inp.getType().equalsIgnoreCase("dgchk"))
                    ret = String.valueOf("1");
            if (inp.getField().equalsIgnoreCase("FIRM_SPATO") && this._doc.getCDoc().startsWith("S")) {
                    ret = Utils.isEmpty(this._pay.getCTerrit()) ? "" : this._pay.getCTerrit();
            }
            if (inp.getField().equalsIgnoreCase("OBL") && this._doc.getCDoc().startsWith("S")) {
                    ret = Utils.isEmpty(cterrit) ? "00" : (cterrit.length() > 5 ? cterrit.substring(0, 2) : "00");;
            }
            if (inp.getField().equalsIgnoreCase("RAY") && this._doc.getCDoc().startsWith("S")) {
                    ret = Utils.isEmpty(cterrit) ? "000" : (cterrit.length() > 5 ? cterrit.substring(2, 5) : "000");
            }

            if (!Utils.isEmpty(inp.getInitVal())) {
                String cmd = inp.getInitVal();
                List<String> itm = RegexpUtl.getMatches(cmd, "(#\\{\\w+.\\w+\\})");
                if (!itm.isEmpty()) {
                    for(String exp : itm) {
                        Pattern pattern = Pattern.compile("#\\{(\\w+).(\\w+)\\}");
                        Matcher matcher = pattern.matcher(exp);
                        if(matcher.matches()) {
                            String cnxt = matcher.group(1), prop = matcher.group(2);
                            if (cnxt.equalsIgnoreCase("pay")) {
                                switch(prop.toUpperCase()) {
                                    case "TIN_UR"://  if (_pay.getFacemode() == 1) ret = _pay.getTin(); break;
                                    case "TIN_FIZ":// if (_pay.getFacemode() == 2) ret = _pay.getTin(); break;
                                    case "TIN": ret = _pay.getTin(); break;                                        
                                    case "NAME": ret = _pay.getName(); break;
                                    case "NAME_UR": if (_pay.getFacemode() == 1) ret = _pay.getName(); break;
                                    case "NAME_FIZ": if (_pay.getFacemode() == 2) ret = _pay.getName(); break;
                                    case "KVED": ret = _pay.getKved(); break;
                                    case "KVEDNAME": ret = _pay.getKvedName(); break;
                                    case "OPF": ret = _pay.getOpf(); break;
                                    case "OPFNAME": ret = _pay.getOpfName(); break;
                                    case "COOGU": ret = _pay.getCoogu(); break;
                                    case "COOGUNAME": ret = _pay.getCooguName(); break;
                                    case "KFV": ret = _pay.getKfv(); break;
                                    case "KFVNAME": ret = _pay.getKfvName(); break;
                                    case "OKONX": ret = _pay.getOkonx(); break;
                                    case "OKONXNAME": ret = _pay.getOkonxName(); break;    
                                    case "ADDRESS": ret = _pay.getAddress(); break;
                                    case "ZIP": ret = _pay.getZip(); break;
                                    case "TEL": ret = _pay.getTel(); break;
                                    case "FAX": ret = !Utils.isEmpty(_pay.getFax()) ? _pay.getFax() : _pay.getTel(); break;
                                    case "EMAIL": ret = _pay.getEmail(); break;
                                    case "FULLSTINAME": ret = _pay.getCSti() + " " + _pay.getStiName(); break;
                                    case "TINSTI" :    
                                    case "STITIN" :
                                        ret = _pay.getStiTin(); break;
                                    case "STI" : ret = String.valueOf(_pay.getCSti()); break;
                                    case "STINAME" : ret = _pay.getStiName(); break;
                                    case "KOATUU" : ret = _pay.getCTerrit(); break;
                                    case "KOATUUNAME" : ret = _pay.getTerritName(); break;
                                    case "DIRINN": ret = _pay.getInnDir(); break;
                                    case "BUHINN": ret = _pay.getInnBuh(); break;
                                    case "DIRPIB": ret = _pay.getPibDir(); break;
                                    case "BUHPIB": ret = _pay.getPibBuh(); break;
                                    case "DIRTEL": ret = !Utils.isEmpty(_pay.getTelDir()) ? _pay.getTelDir() : _pay.getTel(); break;
                                    case "BUHTEL": ret = !Utils.isEmpty(_pay.getTelBuh()) ? _pay.getTelBuh() : _pay.getTel(); break;    
                                    case "REGNAME": ret = _pay.getAdrRegionName(); break;
                                    case "VATID": ret = _pay.getVatId(); break;
                                    case "VATNOMPP": ret = _pay.getVatNompp(); break;
                                    case "BANKMFO": ret = _pay.getBankMfo(); break;
                                    case "BANKNAME": ret = _pay.getBankName(); break;
                                    case "BANKACCOUNT": ret = _pay.getBankAccount(); break;
                                    default: ret = exp; break;
                                }
                            } else if (cnxt.equalsIgnoreCase("prm")) {
                                if (prop.equalsIgnoreCase("currdate")) {
                                    ret = PzUtils.getCurrDateAsXmlStr();
                                    ret = ret.replace(".", "");
                                } else if (prop.equalsIgnoreCase("currtime")) {
                                    ret = PzUtils.getCurrTimeAsXmlStr();
                                } else if (prop.equalsIgnoreCase("perYear")) {
                                    ret = String.valueOf(_doc.getPeriodYear());
                                } else if (prop.equalsIgnoreCase("perMonth")) {
                                    ret = String.valueOf(_doc.getPeriodMonth());
                                } else if (prop.equalsIgnoreCase("perMonthNm")) {
                                    ret = getPeriodMonthNm(0);
                                } else if (prop.equalsIgnoreCase("perTypeNm")) {
                                    ret = getPeriodTypeNm();
                                } else if (prop.equalsIgnoreCase("perPeriodNm")) {
                                    ret = getPeriodNm(0);
                                } else if (prop.equalsIgnoreCase("lastday")) {
                                    Calendar cal = _getLastDate();
                                    ret = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
                                    ret = ret.replace(".", "");
                                } else if (prop.equalsIgnoreCase("perStatLastDay") || prop.equalsIgnoreCase("lastPeriodDate")) {
                                    DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
                                    Calendar cal = _getLastDate();
                                    ret = dateFormat.format(cal.getTime());
                                } else if (prop.equalsIgnoreCase("lastPeriodDateAsStr")) {
                                    Calendar cal = _getLastDate();
                                    ret = cal.get(Calendar.DAY_OF_MONTH) + " " + _getMonthUANameR(cal.get(Calendar.MONTH)) + " " + cal.get(Calendar.YEAR);    
                                } else if (prop.equalsIgnoreCase("lastPeriodDateWithoutYear")) {
                                    Calendar cal = _getLastDate();
                                    ret = cal.get(Calendar.DAY_OF_MONTH) + " " + _getMonthUANameR(cal.get(Calendar.MONTH));                                    
                                } else if (prop.equalsIgnoreCase("statPerType")) {
                                    ret = String.valueOf(getStatPerType());
                                } else if (prop.equalsIgnoreCase("firstDateOfNextMonth")) {
                                    ret = getFirstDateOfNextMonthAsDate();
                                } else if (prop.equalsIgnoreCase("firstFullDateOfNextMonth")) {
                                    ret = getFirstDateOfNextMonthAsStr();
                                } else if (prop.equalsIgnoreCase("firstFullDateOfNextMonthWithoutDay")) {
                                    Calendar cal = _getFirstDateOfNextMonth(1);
                                    cal.getTime();
                                    ret = _getMonthUANameR(cal.get(Calendar.MONTH)) + " " + cal.get(Calendar.YEAR);
                                } else if (prop.equalsIgnoreCase("paramMyDate21")) {
                                    if (_doc.getPeriodType() == 2 && (_doc.getPeriodMonth() == 6 || _doc.getPeriodMonth() == 12)) {
                                        Calendar cal = _getFirstDateOfNextMonth(1);
                                        ret = _getMonthUANameR(cal.get(Calendar.MONTH)) + " " + cal.get(Calendar.YEAR);
                                    } else {
                                        ret = "";
                                    }
                                } else if (prop.equalsIgnoreCase("lastPeriodMonthAndYear")) {
                                    ret = getLastPeriodMonthAndYear();
                                } else {
                                    ret = ""; // exp;
                                }
                            } else {
                                ret = ""; //exp;
                            }
                            cmd = cmd.replace("#{" + cnxt + "." + prop + "}", ret);
                        } else {
                            cmd = ""; // exp;
                        }                        
                    }
                } else {
                    Pattern pattern = Pattern.compile("#\\{(\\w+).(\\w+)\\((\\d+)\\)\\}");
                    Matcher matcher = pattern.matcher(cmd);
                    if(matcher.matches()) {
                        String cnxt = matcher.group(1), prop = matcher.group(2), arg = matcher.group(3);
                        if (cnxt.equalsIgnoreCase("prm")) {
                            if (prop.equalsIgnoreCase("perMonthNm")) {
                                ret = getPeriodMonthNm(Integer.valueOf(arg));
                            } else if (prop.equalsIgnoreCase("perPeriodNm")) {
                                ret = getPeriodNm(Integer.valueOf(arg));
                            } else if (prop.equalsIgnoreCase("perYear")) {
                                ret = String.valueOf(getPeriodYY(Integer.valueOf(arg)));
                            } else if (prop.equalsIgnoreCase("perMonth")) {
                                ret = String.valueOf(getPeriodMM(Integer.valueOf(arg)));
                            } else {
                                ret = cmd;
                            }
                            cmd = cmd.replace("#{" + cnxt + "." + prop + "(" + arg + ")}", ret);
                        } 
                    }
                }
                ret = cmd;
            }
        }
        return ret;
    }

    public String getDefaultValue (String fieldNm) {
        PZDocInput inp = null;
        for (PZDocInput var : _docVars) {
            if (var.getField().equalsIgnoreCase(fieldNm)) {
                inp = var;
                break;
            }
        }
        return getDefaultValue(inp);
    }

    public int getPerType() { return _doc.getPeriodType(); }

    public int getPerMonth() { return _doc.getPeriodMonth(); }

    public int getPerYear() { return _doc.getPeriodYear(); }

    public int getPeriodYY(int delta) {
        int perMonth = _doc.getPeriodMonth(), perYear = _doc.getPeriodYear();
        if (delta != 0) {
            if ((perMonth + delta) > 12) {
                perMonth = delta + perMonth - 12; perYear++;
            } else if ((perMonth + delta) <= 0) { 
                perMonth = 12 + perMonth + delta;
            } else {
                perMonth += delta;
            }
        }
        return perYear;
    }

    public int getPeriodMM (int delta) {
        int perMonth = _doc.getPeriodMonth(), perYear = _doc.getPeriodYear();

        if (delta != 0) {
            if ((perMonth + delta) > 12) {
                perMonth = delta + perMonth - 12; perYear++;
            } else if ((perMonth + delta) <= 0) { 
                perMonth = 12 + perMonth + delta;
            } else {
                perMonth += delta;
            }
        }
        return perMonth;
    }

    public String getPeriodMonthNm(int delta) {
        int perMonth = getPeriodMM(delta);
        return PzUtils.getPeriodMonthNm(perMonth, 1);
    }

    public String getPeriodTypeNm() {
        return PzUtils.getPeriodMonthNm(_doc.getPeriodMonth(), _doc.getPeriodType());
    }
    
    public String getPeriodNm (int delta) {
        int perMonth = _doc.getPeriodMonth(), perYear = _doc.getPeriodYear(), perType = _doc.getPeriodType();

        if (delta != 0) {
            if ((perMonth + delta) > 12) {
                perMonth = delta + perMonth - 12; perYear++;
            } else if ((perMonth + delta) <= 0) { 
                perMonth = 12 + perMonth + delta;
            } else {
                perMonth += delta;
            }
        }
        return PzUtils.getPeriodMonthNm(perMonth, perType) + " " + perYear;
    }

    public String getFirstDateOfNextMonthAsDate() {
        DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
        Calendar cal = _getFirstDateOfNextMonth(1);
        return dateFormat.format(cal.getTime());        
    }
    
    public String getLastPeriodMonthAndYear() {
        Calendar cal = _getFirstDateOfNextMonth(0);
        cal.getTime();
        return _getMonthUAName(cal.get(Calendar.MONTH)) + " " + cal.get(Calendar.YEAR);
    }

    public String getFirstDateOfNextMonthAsStr() {
        Calendar cal = _getFirstDateOfNextMonth(1);
        cal.getTime();
        return cal.get(Calendar.DAY_OF_MONTH) + " " + _getMonthUANameR(cal.get(Calendar.MONTH)) + " " + cal.get(Calendar.YEAR);
    }    
    
    public int getStatPerType () {
        int ret = 0;
        switch(_doc.getPeriodType()) {
            case 1: ret = 0; break;
            case 2: ret = 10; break;
            case 3: ret = 20; break;
            case 4: ret = 25; break;
            case 5: ret = 30; break;
            default: ret = _doc.getPeriodType(); break;
        }
        return ret;
    } 
    
    private Calendar _getFirstDateOfNextMonth(int cntMonth) {
        int perMonth = _doc.getPeriodMonth(), perYear = _doc.getPeriodYear();

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, perMonth - 1);
        cal.set(Calendar.YEAR, perYear);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        if (cntMonth > 0) { cal.add(Calendar.MONTH, cntMonth); }       
        
        return cal;
    }
    
    private Calendar _getLastDate() {
        int perMonth = _doc.getPeriodMonth(), perYear = _doc.getPeriodYear();

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, perMonth - 1);
        cal.set(Calendar.YEAR, perYear);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));

        return cal;
    }

    private String _getMonthUAName(int month){
        String[] monthNames = { "січень", "лютий", "березень", "квітень", "травень", "червень", "липень", "серпень", "вересень", "жовтень", "листопад", "грудень" };
        return monthNames[month];
    }

    private String _getMonthUANameR(int month){
        String[] monthNames = { "січня", "лютого", "березня", "квітня", "травня", "червня", "липня", "серпня", "вересня", "жовтня", "листопада", "грудня" };
        return monthNames[month];
    }     
}
