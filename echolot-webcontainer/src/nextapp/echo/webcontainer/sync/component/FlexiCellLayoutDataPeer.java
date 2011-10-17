package nextapp.echo.webcontainer.sync.component;

import de.exxcellent.echolot.layout.FlexiCellLayoutData;
import nextapp.echo.app.serial.SerialException;
import nextapp.echo.app.serial.SerialUtil;
import nextapp.echo.app.serial.property.CellLayoutDataPeer;
import nextapp.echo.app.util.Context;
import org.w3c.dom.Element;

/**
 *
 * @author sieskei (XSoft Ltd.)
 */
public class FlexiCellLayoutDataPeer extends CellLayoutDataPeer {

    @Override
    public void toXml(Context context, Class objectClass, Element propertyElement, Object propertyValue) throws SerialException {
        super.toXml(context, objectClass, propertyElement, propertyValue);
        FlexiCellLayoutData layoutData = (FlexiCellLayoutData) propertyValue;
        SerialUtil.toXml(context, FlexiCellLayoutData.class, propertyElement, "height", layoutData.getHeight());
        SerialUtil.toXml(context, FlexiCellLayoutData.class, propertyElement, "width", layoutData.getWidth());
    }
}
