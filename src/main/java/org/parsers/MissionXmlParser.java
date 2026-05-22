package org.parsers;

import org.model.Curse;
import org.model.Mission;
import org.model.MissionBuilder;
import org.model.Sorcerer;
import org.model.Technique;
import org.source.MissionDataSource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;

public class MissionXmlParser extends AbstractParser<Mission> {
    @Override
    public boolean canParse(MissionDataSource source) {
        return source.getIdentifier().endsWith(".xml");
    }

    @Override
    protected Mission parseFromStream(InputStream inputStream) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringElementContentWhitespace(true);
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        Document document = factory.newDocumentBuilder().parse(inputStream);
        Element root = document.getDocumentElement();

        MissionBuilder builder = new MissionBuilder();
        builder.setMissionId(childText(root, "missionId"));
        builder.setDate(childText(root, "date"));
        builder.setLocation(childText(root, "location"));
        builder.setOutcome(childText(root, "outcome"));
        builder.setDamageCost(parseLong(childText(root, "damageCost")));
        builder.setComment(firstNotBlank(childText(root, "comment"), childText(root, "note")));

        Element curseElement = childElement(root, "curse");
        if (curseElement != null) {
            builder.setCurse(new Curse(childText(curseElement, "name"), childText(curseElement, "threatLevel")));
        }

        NodeList sorcererNodes = root.getElementsByTagName("sorcerer");
        for (int i = 0; i < sorcererNodes.getLength(); i++) {
            Element element = (Element) sorcererNodes.item(i);
            builder.addSorcerer(new Sorcerer(childText(element, "name"), childText(element, "rank")));
        }

        NodeList techniqueNodes = root.getElementsByTagName("technique");
        for (int i = 0; i < techniqueNodes.getLength(); i++) {
            Element element = (Element) techniqueNodes.item(i);
            builder.addTechnique(new Technique(
                    childText(element, "name"),
                    childText(element, "type"),
                    childText(element, "owner"),
                    parseLong(childText(element, "damage"))
            ));
        }
        return builder.build();
    }

    private Element childElement(Element parent, String tagName) {
        NodeList children = parent.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            if (node instanceof Element element && tagName.equals(element.getTagName())) {
                return element;
            }
        }
        return null;
    }

    private String childText(Element parent, String tagName) {
        Element child = childElement(parent, tagName);
        return child == null ? null : child.getTextContent().trim();
    }

    private long parseLong(String text) {
        if (text == null || text.isBlank()) {
            return 0;
        }
        return Long.parseLong(text.trim());
    }

    private String firstNotBlank(String first, String second) {
        return first != null && !first.isBlank() ? first : second;
    }
}
