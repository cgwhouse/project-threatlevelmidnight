package edu.wofford;

import java.util.*;
import java.io.*;
import javax.xml.stream.*;
import javax.xml.stream.events.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import java.text.SimpleDateFormat;

/**
 * XML is a class containing static functions used by {@link ArgumentParser} to parse and create XML files.
 */
public class XML {

    /**
     * Reads argument information from an XML file and adds them to the ArgumentParser object's known arguments.
     * 
     * @param filename the name of the file to read from
     * @param parser   ArgumentParser object that we want to give the argument information to
     */
    public static void parseXML(String filename, ArgumentParser parser) {
        int positionalCount = 0;
        String[] fields = { "name", "shortname", "type", "position", "default", "required", "mutex", "accepted" };
        Set<String> set = new HashSet<String>(Arrays.asList(fields));
        Set<String> accepted = new HashSet<String>();
        Set<String> mutex = new HashSet<String>();
        Map<Integer, Argument> posMap = new HashMap<Integer, Argument>();
        Map<String, String> attMap = new HashMap<String, String>();

        for (int i = 0; i < fields.length - 3; i++) {
            attMap.put(fields[i], "");
        }
        attMap.put("required", "false");
        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLEventReader eventReader = factory.createXMLEventReader(new FileReader(filename));
            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();
                switch (event.getEventType()) {
                case XMLStreamConstants.START_ELEMENT:
                    StartElement startElement = event.asStartElement();
                    String qName = startElement.getName().getLocalPart().toLowerCase();
                    if (qName.equals("positional") || qName.equals("named")) {
                        accepted.clear();
                        mutex.clear();
                        attMap.put("required", "false");
                    }
                    if (set.contains(qName)) {
                        if (qName.equals("required")) {
                            attMap.put("required", "true");
                        } else {
                            handleCharacters(qName, eventReader.nextEvent(), attMap, accepted, mutex);
                        }
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    EndElement endElement = event.asEndElement();
                    if (endElement.getName().getLocalPart().equalsIgnoreCase("positional")
                            || endElement.getName().getLocalPart().equalsIgnoreCase("named")) {
                        if (endElement.getName().getLocalPart().equalsIgnoreCase("positional")) {
                            positionalCount++;
                            setPositionalFromXML(attMap, posMap, accepted);
                        } else if (endElement.getName().getLocalPart().equalsIgnoreCase("named")) {
                            setNamedFromXML(attMap, accepted, mutex, parser);
                        }
                    }
                    break;
                }
            }
            for (int i = 1; i < positionalCount + 1; i++) {
                Argument arg = posMap.get(i);
                parser.setArgument(arg);
            }
        } catch (FileNotFoundException | XMLStreamException e) {
            throw new BadXMLException();
        }
    }

    /**
     * Creates a string that contains all of the parser's argument information, formatted as XML.
     * 
     * @param createFile     if true, XML string is written to a newly created file in the current directory
     * @param positionalArgs list of the parser's positional arguments
     * @param namedArgs      list of the parser's named arguments
     * @param argumentMap    map whose keys are the names of the parser's arguments, values are Argument objects
     * @return           string of parser's argument information formatted as XML
     */
    public static String createXML(boolean createFile, List<String> positionalArgs, List<String> namedArgs,
            Map<String, Argument> argumentMap) {
        try {
            StringWriter stringWriter = new StringWriter();
            XMLOutputFactory xMLOutputFactory = XMLOutputFactory.newInstance();
            XMLStreamWriter xmlStreamWriter = xMLOutputFactory.createXMLStreamWriter(stringWriter);
            int position = 1;
            xmlStreamWriter.writeStartDocument();
            xmlStreamWriter.writeStartElement("arguments");
            for (String name : positionalArgs) {
                writePositionalXML(name, xmlStreamWriter, position, argumentMap);
                position++;
            }
            for (String name : namedArgs) {
                writeNamedXML(name, xmlStreamWriter, argumentMap);
            }
            xmlStreamWriter.writeEndElement();
            xmlStreamWriter.writeEndDocument();
            xmlStreamWriter.flush();
            xmlStreamWriter.close();
            String xmlString = stringWriter.getBuffer().toString().replace("<?xml version=\"1.0\" ?>", "");
            stringWriter.close();
            if (createFile) {
                createXMLFile(xmlString);
            }
            return xmlString;
        } catch (IOException | XMLStreamException e) {
            throw new BadXMLException();
        }
    }

    //region Private Methods
    private static void replaceBadXML(String timeStamp) {
        String oldFileName = "tmp.xml";
        String newFileName = "xml-" + timeStamp + ".xml";
        BufferedReader br = null;
        BufferedWriter bw = null;
        try {
            br = new BufferedReader(new FileReader(oldFileName));
            bw = new BufferedWriter(new FileWriter(newFileName));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"))
                    line = line.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "");
                bw.write(line + "\n");
            }
            if (br != null) {
                br.close();
            }
            if (bw != null) {
                bw.close();
            }
            File oldFile = new File(oldFileName);
            oldFile.delete();
        } catch (IOException e) {
            throw new BadXMLException();
        }
    }

    private static void writePositionalXML(String name, XMLStreamWriter writer, int position,
            Map<String, Argument> argumentMap) {
        try {
            Argument arg = argumentMap.get(name);
            writer.writeStartElement("positional");
            writer.writeStartElement("name");
            writer.writeCharacters(arg.getName());
            writer.writeEndElement();
            writer.writeStartElement("type");
            writer.writeCharacters(arg.getType());
            writer.writeEndElement();
            writer.writeStartElement("position");
            writer.writeCharacters(Integer.toString(position));
            writer.writeEndElement();
            if (!arg.getAcceptedValues().isEmpty()) {
                for (String value : arg.getAcceptedValues()) {
                    writer.writeStartElement("accepted");
                    writer.writeCharacters(value);
                    writer.writeEndElement();
                }
            }
            writer.writeEndElement();
        } catch (XMLStreamException e) {
            throw new BadXMLException();
        }
    }

    private static void writeNamedXML(String name, XMLStreamWriter writer, Map<String, Argument> argumentMap) {
        NamedArgument arg = (NamedArgument) argumentMap.get(name);
        try {
            writer.writeStartElement("named");
            writer.writeStartElement("name");
            writer.writeCharacters(arg.getName().substring(2));
            writer.writeEndElement();
            if (!arg.getNicknames().equals("-")) {
                for (int i = 1; i < arg.getNicknames().length(); i++) {
                    writer.writeStartElement("shortname");
                    writer.writeCharacters(Character.toString(arg.getNicknames().charAt(i)));
                    writer.writeEndElement();
                }
            }
            writer.writeStartElement("type");
            writer.writeCharacters(arg.getType());
            writer.writeEndElement();
            if (arg.isRequired()) {
                writer.writeStartElement("required");
                writer.writeEndElement();
            } else {
                writer.writeStartElement("default");
                writer.writeCharacters(arg.getDefault());
                writer.writeEndElement();
            }
            if (arg.hasMutualExclusiveArgs()) {
                for (Map.Entry<String, Argument> pair : argumentMap.entrySet()) {
                    if (pair.getKey().startsWith("--") && !pair.getKey().equals(arg.getName())) {
                        NamedArgument namedArg = (NamedArgument) pair.getValue();
                        if (namedArg.hasMutualExclusiveArgs() && namedArg.isMutuallyExclusive(arg)) {
                            writer.writeStartElement("mutex");
                            writer.writeCharacters(namedArg.getName().substring(2));
                            writer.writeEndElement();
                        }
                    }
                }
            }
            if (!arg.getAcceptedValues().isEmpty()) {
                for (String value : arg.getAcceptedValues()) {
                    writer.writeStartElement("accepted");
                    writer.writeCharacters(value);
                    writer.writeEndElement();
                }
            }
            writer.writeEndElement();
        } catch (XMLStreamException e) {
            throw new BadXMLException();
        }
    }

    private static void createXMLFile(String xml) {
        try {
            Source xmlInput = new StreamSource(new StringReader(xml));
            StreamResult xmlOutput = new StreamResult(new StringWriter());
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.transform(xmlInput, xmlOutput);
            String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
            PrintWriter out = new PrintWriter("tmp.xml");
            out.print(xmlOutput.getWriter().toString());
            out.close();
            replaceBadXML(timeStamp);
        } catch (TransformerException | FileNotFoundException e) {
            throw new BadXMLException();
        }
    }

    private static void handleCharacters(String name, XMLEvent event, Map<String, String> attMap, Set<String> acc,
            Set<String> mutex) {
        Characters characters = event.asCharacters();
        switch (name) {
        case "accepted":
            acc.add(characters.getData());
            break;
        case "mutex":
            mutex.add("--" + characters.getData());
            break;
        default:
            attMap.put(name, characters.getData());
            break;
        }
    }

    private static void setPositionalFromXML(Map<String, String> attMap, Map<Integer, Argument> posMap,
            Set<String> acc) {
        Argument arg = new Argument(attMap.get("name"));
        arg.setType(attMap.get("type"));
        String[] array = acc.toArray(new String[acc.size()]);
        arg.addAcceptedValues(array);
        posMap.put(Integer.parseInt(attMap.get("position")), arg);
    }

    private static void setNamedFromXML(Map<String, String> attMap, Set<String> acc, Set<String> mutex,
            ArgumentParser parser) {
        NamedArgument arg;
        if (attMap.get("required").equals("true")) {
            arg = new NamedArgument("--" + attMap.get("name"));
        } else {
            arg = new NamedArgument("--" + attMap.get("name"), attMap.get("default"));
        }
        arg.setType(attMap.get("type"));
        String[] accepted = acc.toArray(new String[acc.size()]);
        arg.addAcceptedValues(accepted);
        if (!mutex.isEmpty()) {
            String[] mutexes = mutex.toArray(new String[mutex.size()]);
            for (int i = 0; i < mutexes.length; i++) {
                arg.addMutuallyExclusiveArg(mutexes[i]);
            }
        }
        if (attMap.containsKey("shortname")) {
            if (!attMap.get("shortname").equals("")) {
                parser.setNickname(arg, "-" + attMap.get("shortname"));
            } else {
                parser.setArgument(arg);
            }
        } else {
            parser.setArgument(arg);
        }
    }
    //endregion

}
