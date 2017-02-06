import android.util.Xml;

import com.dayteam.goeng.model.Event;

import org.apache.commons.lang3.StringUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.functions.Function;

/**
 * Example of streaming parsing of very big XML file (> 80mb)
 * Maps {@link InputStream} of XML to objects
 */
public class XmlToObjectsConverter implements Function<InputStream, List<Event>> {

    @Override
    public List<Event> apply(InputStream inputStream) throws IOException, XmlPullParserException, ParseException {
        List<Event> result = new ArrayList<>();
        HashMap<String, String> teacherList = new HashMap<>();
        HashMap<String, String> subjectList = new HashMap<>();
        HashMap<String, String> courseList = new HashMap<>();
        HashMap<String, String> currentCollection = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(inputStream, null);

            int eventType = parser.getEventType();
            Event e = new Event();
            String text;
            String tagName = null;
            while (eventType != XmlPullParser.END_DOCUMENT){
                if(eventType == XmlPullParser.START_TAG || eventType == XmlPullParser.END_TAG) {
                    tagName = parser.getName();
                }
                switch (eventType){
                    case XmlPullParser.START_TAG: {
                        if (tagName != null && "event".equalsIgnoreCase(tagName)) {
                            e = new Event();
                            break;
                        } else if("coll_options".equalsIgnoreCase(tagName) && "coursesList".equalsIgnoreCase(parser.getAttributeValue(null, "for"))){
                            currentCollection = courseList;
                            e = null;
                        } else if("coll_options".equalsIgnoreCase(tagName) && "subjectsList".equalsIgnoreCase(parser.getAttributeValue(null, "for"))){
                            currentCollection = subjectList;
                            e = null;
                        } else if("coll_options".equalsIgnoreCase(tagName) && "teachersList".equalsIgnoreCase(parser.getAttributeValue(null, "for"))){
                            currentCollection = teacherList;
                            e = null;
                        } else if("item".equalsIgnoreCase(tagName) && currentCollection != null){
                            currentCollection.put(parser.getAttributeValue(null, "value"), parser.getAttributeValue(null, "label"));
                        }
                        break;
                    }
                    case XmlPullParser.TEXT:{
                        text = parser.getText();

                        if ("start_date".equalsIgnoreCase(tagName)) {
                            e.setStartDate(sdf.parse(text).getTime());
                        } else if ("end_date".equalsIgnoreCase(tagName)) {
                            e.setEndDate(sdf.parse(text).getTime());
                        } else if ("text".equalsIgnoreCase(tagName)) {
                            e.setText(text);
                        } else if ("id".equalsIgnoreCase(tagName)) {
                            e.setId(text);
                        } else if ("cource_id".equalsIgnoreCase(tagName)) {
                            e.setCourceId(text);
                        } else if ("subject_id".equalsIgnoreCase(tagName)) {
                            e.setSubjectId(text);
                        } else if ("info_for_contacts".equalsIgnoreCase(tagName)) {
                            e.setInfoForContacts(text);
                        } else if ("audience".equalsIgnoreCase(tagName)) {
                            e.setAudience(text);
                        } else if ("remaining_seats".equalsIgnoreCase(tagName)) {
                            //do nothing
                        } else if ("cost".equalsIgnoreCase(tagName)) {
                            //do nothing
                        } else if ("number_seats".equalsIgnoreCase(tagName)) {
                            //do nothing
                        } else if ("employees_ids".equalsIgnoreCase(tagName)) {
                            e.setEmployeesIds(text);
                        } else if ("already_calculate".equalsIgnoreCase(tagName)) {
                            //do nothing
                        } else if ("rec_type".equalsIgnoreCase(tagName)) {
                            //do nothing
                        } else if ("event_pid".equalsIgnoreCase(tagName)) {
                            //do nothing
                        } else if ("url_c".equalsIgnoreCase(tagName)) {
                            //do nothing
                        } else if ("calendar_hidden".equalsIgnoreCase(tagName)) {
                            //do nothing
                        } else if ("calendar_written_link_disable".equalsIgnoreCase(tagName)) {
                            //do nothing
                        } else if ("filial".equalsIgnoreCase(tagName)) {
                            e.setFilial(text);
                        } else if ("contacts_ids".equalsIgnoreCase(tagName)) {
                            //do nothing
                        } else if ("class_status".equalsIgnoreCase(tagName)) {
                            e.setClassStatus(text);
                        } else if ("class_description".equalsIgnoreCase(tagName)) {
                            e.setClassDescription(text);
                        }
                        break;
                    }
                    case XmlPullParser.END_TAG:{
                        if("event".equalsIgnoreCase(tagName)){
                            result.add(e);
                        }
                        break;
                    }
                    default:
                        break;
                }
                eventType = parser.next();
            }

        } finally {
            inputStream.close();
        }

        for(Event event : result){
            String courceId = event.getCourceId();
            String employeesIds = event.getEmployeesIds();
            String subjectId = event.getSubjectId();
            String[] targetValues = new String[]{courceId, employeesIds, subjectId};

            final int AMOUNT_OF_PARAMETERS = targetValues.length;
            String[] exactValues = null;
            HashMap<String, String>[] targetCollections = new HashMap[]{courseList, teacherList, subjectList};

            exactValues = new String[AMOUNT_OF_PARAMETERS];
            for(int i = 0; i < targetValues.length; i++) {
                String targetValue = targetValues[i];
                if (StringUtils.isNotEmpty(targetValue)){
                    exactValues[i] = targetCollections[i].get(targetValues[i]);
                }
            }
            event.setCourceName(exactValues[0]);
            event.setEmployeesNames(exactValues[1]);
            event.setSubjectName(exactValues[2]);
        }

        return result;
    }

    private Event readEvent(){
        return null;
    }

    public static Function<InputStream, List<Event>> create(){
        return new XmlToObjectsConverter();
    }


}
