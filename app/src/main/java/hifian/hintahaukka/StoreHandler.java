package hifian.hintahaukka;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Reads the inputstream XML data and converts it into Store objects
 */
public class StoreHandler extends DefaultHandler{

    private List<Store> stores = null;
    private Store store = null;
    private String elementValue;

    @Override
    public void startDocument() throws SAXException {
        stores = new ArrayList<Store>();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase("node")) {
            store = new Store();

            if(attributes.getLength() > 0)
            {
                String storeId = attributes.getValue("id");
                double lat = Double.parseDouble(attributes.getValue("lat"));
                double lon = Double.parseDouble(attributes.getValue("lon"));
                store.setStoreId(storeId);
                store.setLat(lat);
                store.setLon(lon);
            }
        } else if (qName.equalsIgnoreCase("tag")) {
            if (attributes.getValue("k").equals("name")) {
                String name = attributes.getValue("v");
                store.setName(name);
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("node")) {
            stores.add(store);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        elementValue = new String(ch, start, length);
    }

    public List<Store> getStores() {
        return stores;
    }

}
