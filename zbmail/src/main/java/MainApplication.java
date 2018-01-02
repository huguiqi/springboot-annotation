import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

import static org.apache.poi.ss.usermodel.Row.MissingCellPolicy.RETURN_BLANK_AS_NULL;

/**
 * Created by guiqi on 2017/12/29.
 */
public class MainApplication {

    private final static String MAIL_SERVER_HOST = "smtp.qiye.163.com";
    private final static String USER = "huguiqi@shanlinjinrong.com";
    private final static String PASSWORD = "MMHuguiqi_198838";
    private final static String MAIL_FROM = "huguiqi@shanlinjinrong.com"; //������
    private final static String MAIL_TO = "guiqi.hu@163.com"; //�ռ���
    private final static String MAIL_CC = "417980991@qq.com"; //������
    private final static String MAIL_BCC = ""; //������

    public static void sendMail() throws Exception {
        Properties prop = new Properties();
        prop.setProperty("mail.debug", "true");
        prop.setProperty("mail.host", MAIL_SERVER_HOST);
        prop.setProperty("mail.transport.protocol", "smtp");
        prop.setProperty("mail.smtp.auth", "true");
        // 1������session
        Session session = Session.getInstance(prop);
        Transport ts = null;
        // 2��ͨ��session�õ�transport����
        ts = session.getTransport();
        // 3�������ʼ�������
        ts.connect(MAIL_SERVER_HOST, USER, PASSWORD);
        // 4�������ʼ�
        MimeMessage message = new MimeMessage(session);
        // �ʼ���Ϣͷ
        message.setFrom(new InternetAddress(MAIL_FROM)); // �ʼ��ķ�����
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(MAIL_TO)); // �ʼ����ռ���
        message.addRecipient(Message.RecipientType.CC, new InternetAddress(MAIL_CC)); // �ʼ��ĳ�����
//        message.setRecipient(Message.RecipientType.BCC, new InternetAddress(MAIL_BCC)); // �ʼ���������

        HashMap<String,String> map = givenMailContent();
        message.setSubject(map.get("title")); // �ʼ��ı���
        // �ʼ���Ϣ��
        message.setText(map.get("text"));
        // 5�������ʼ�
        ts.sendMessage(message, message.getAllRecipients());
        ts.close();

    }


    private static HashMap<String,String> givenMailContent(){
        StringBuilder sb = new StringBuilder("��ã�\n");
        Date todayDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy��MM��dd��");
        sb.append("     ")
          .append(format.format(todayDate))
          .append("���ܹ����ܽ����������\n")
          .append("\n")
          .append("лл������");

        HashMap<String,String> hashMap = new HashMap<String, String>();
        hashMap.put("text",sb.toString());
        format.applyPattern("yyyy-MM-dd");
        hashMap.put("title","������-�������ܱ�("+format.format(todayDate)+")");
        return hashMap;
    }

    /**
     * ��ȡ��Ŀ����·��(����jar)
     *
     * @return
     */
    public static String getProjectPath() {

        java.net.URL url = MainApplication.class.getProtectionDomain().getCodeSource()
                .getLocation();
        String filePath = null;
        try {
            filePath = java.net.URLDecoder.decode(url.getPath(), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (filePath.endsWith(".jar"))
            filePath = filePath.substring(0, filePath.lastIndexOf("/") + 1);
        java.io.File file = new java.io.File(filePath);
        filePath = file.getAbsolutePath();
        return filePath;
    }

    /**
     * ��ȡ��Ŀ����·��
     *
     * @return
     */
    public static String getRealPath() {
        String realPath = MainApplication.class.getClassLoader().getResource("")
                .getFile();
        java.io.File file = new java.io.File(realPath);
        realPath = file.getAbsolutePath();
        try {
            realPath = java.net.URLDecoder.decode(realPath, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return realPath;
    }

    public static String getAppPath(Class<?> cls) {
        // ����û�����Ĳ����Ƿ�Ϊ��
        if (cls == null)
            throw new java.lang.IllegalArgumentException("��������Ϊ�գ�");

        ClassLoader loader = cls.getClassLoader();
        // ������ȫ������������
        String clsName = cls.getName();
        // �˴����ж��Ƿ���Java������⣬��ֹ�û�����JDK���õ����
        if (clsName.startsWith("java.") || clsName.startsWith("javax.")) {
            throw new java.lang.IllegalArgumentException("��Ҫ����ϵͳ�࣡");
        }
        // �����class�ļ�ȫ����Ϊ·����ʽ
        String clsPath = clsName.replace(".", "/") + ".class";

        // ����ClassLoader��getResource�������������·����Ϣ�����ļ���
        java.net.URL url = loader.getResource(clsPath);
        // ��URL�����л�ȡ·����Ϣ
        String realPath = url.getPath();
        // ȥ��·����Ϣ�е�Э����"file:"
        int pos = realPath.indexOf("file:");
        if (pos > -1) {
            realPath = realPath.substring(pos + 5);
        }
        // ȥ��·����Ϣ���������ļ���Ϣ�Ĳ��֣��õ������ڵ�·��
        pos = realPath.indexOf(clsPath);
        realPath = realPath.substring(0, pos - 1);
        // ������ļ��������JAR���ļ���ʱ��ȥ����Ӧ��JAR�ȴ���ļ���
        if (realPath.endsWith("!")) {
            realPath = realPath.substring(0, realPath.lastIndexOf("/"));
        }
        java.io.File file = new java.io.File(realPath);
        realPath = file.getAbsolutePath();

        try {
            realPath = java.net.URLDecoder.decode(realPath, "utf-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return realPath;
    }// getAppPath�������


    public static void main(String[] args) throws Exception {
//        File source = new File(givenLastZBPath());
//        File dest = new File(givenTodayZBPath());
//        copyNewZBExcel(source,dest);
        modifyZBExcel(args[0]);
        sendMail();
    }



    private static void copyNewZBExcel(File source, File dest)throws IOException {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(source).getChannel();
            outputChannel = new FileOutputStream(dest).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        } finally {
            inputChannel.close();
            outputChannel.close();
        }
    }

    private static void modifyZBExcel(String nextWeekPlan){
        try {
            File source = new File(givenLastZBPath());
            //poi���µ����ȡexcel�ļ�
            Workbook workbook = new XSSFWorkbook(source);
            Sheet sheet = workbook.getSheetAt(0);
            String headCellValue = sheet.getRow(0).getCell(0).getStringCellValue();
            int weekDayIndex = headCellValue.indexOf("��") + 2;
            int lastWeekDayIndex = headCellValue.indexOf("��") + 2;
            Integer currentWeekDay = new Integer(headCellValue.substring(weekDayIndex, weekDayIndex + 3).trim());
            Integer lastWeekDay = new Integer(headCellValue.substring(lastWeekDayIndex, lastWeekDayIndex + 3).trim());
           String replaceStr = headCellValue.replaceFirst(""+lastWeekDay,(lastWeekDay +1) + "").replaceFirst(""+currentWeekDay,(currentWeekDay +1)+"");

           //ȡֵ
            String planWork = sheet.getRow(3).getCell(1).getStringCellValue();
            String actWork = sheet.getRow(3).getCell(2).getStringCellValue();
            String lastWeekPlanWork = sheet.getRow(11).getCell(1).getStringCellValue();
            String zeRengReng = sheet.getRow(11).getCell(1).getStringCellValue();

            //д������
            sheet.getRow(0).getCell(0).setCellValue(replaceStr);
            sheet.getRow(3).getCell(1).setCellValue(lastWeekPlanWork);
            sheet.getRow(3).getCell(2).setCellValue(lastWeekPlanWork);
            sheet.getRow(11).getCell(1).setCellValue(nextWeekPlan);
            sheet.getRow(11).getCell(2).setCellValue(zeRengReng);

            File dest = new File(givenTodayZBPath());
            //���޸ĺ���ļ�д����D:\\excelĿ¼��
            FileOutputStream os = new FileOutputStream(dest);
            os.flush();
            //��Excelд��
            workbook.write(os);
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String givenLastZBPath(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH,-7);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String path = getProjectPath();
//        return path+"\\�ܹ����ܽἰ�ƻ���_���ֱ�_������_������_"+format.format(calendar.getTime()) +".xlsx";
        return  path+"\\�ܹ����ܽἰ�ƻ���_���ֱ�_������_������_20171222.xlsx";
    }

    private static String givenTodayZBPath(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String path = getProjectPath();
        return path+"\\�ܹ����ܽἰ�ƻ���_���ֱ�_������_������_"+format.format(calendar.getTime())+".xlsx";
    }
}
