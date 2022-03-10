/**
 * Pr&aacute;ctricas de Sistemas Inform&aacute;ticos II
 * VisaCancelacionJMSBean.java
 */

package ssii2.visa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.ejb.EJBException;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.ejb.ActivationConfigProperty;
import javax.jms.MessageListener;
import javax.jms.Message;
import javax.jms.TextMessage;
import javax.jms.JMSException;
import javax.annotation.Resource;
import java.util.logging.Logger;

/**
 * @author jaime
 */
@MessageDriven(mappedName = "jms/VisaColaPagos")
public class VisaCancelacionJMSBean extends DBTester implements MessageListener {
    static final Logger logger = Logger.getLogger("VisaCancelacionJMSBean");
    @Resource
    private MessageDrivenContext mdc;

    // Definir UPDATE sobre la tabla pagos para poner
    // codRespuesta a 999 dado un código de autorización
    private static final String UPDATE_CANCELA_QRY = 
        "update pago " +
        "set codrespuesta = 999 " +
        "where idautorizacion = ?";

    // Obtener el código de respuesta del pago cuyo idAutorizacion 
    // coincida con lo recibido por el mensaje
    private static final String SELECT_CODERES_QRY = 
        "select codrespuesta " +
        "from pago " +
        "where idautorizacion = ?";

    // Rectificar el saldo de la tarjeta que realizó el pago
    private static final String UPDATE_TARJETA_QRY = 
        "update tarjeta " +
        "set saldo = saldo + ? " +
        "where numerotarjeta = ?";


    public VisaCancelacionJMSBean() {
    }

    // Modificarlo para ejecutar el UPDATE definido más arriba,
    // asignando el idAutorizacion a lo recibido por el mensaje
    // Para ello conecte a la BD, prepareStatement() y ejecute correctamente
    // la actualización
    public void onMessage(Message inMessage) {
        TextMessage msg = null;

        /*************************/
        ResultSet rs = null;
        Connection con = null;
        String idReceived = null;
        String codRespuesta = null;
        String numTarjeta = null;
        double importe = 0;
        PreparedStatement pstmt = null;
        /*************************/

        try {
            if (inMessage instanceof TextMessage) {
                msg = (TextMessage) inMessage;
                logger.info("MESSAGE BEAN: Message received: " + msg.getText());

                /*****************************/
                con = getConnection();
                idReceived = msg.getText();

                // Comprobar codigo de respuesta es 000
                pstmt = con.prepareStatement(SELECT_CODERES_QRY);
                pstmt.setString(1, idReceived);
                rs = pstmt.executeQuery();
                if(!rs.next()) {
                    throw new JMSException("ERROR: No existe pago con idAutorizacion = " + idReceived);
                }
                importe = rs.getDouble("importe");
                numTarjeta = rs.getString("numerotarjeta");
                codRespuesta = rs.getString("codrespuesta");
                if(!codRespuesta.equals("000")) {
                    throw new JMSException("ERROR: Codigo de respuesta del pago distinto de 000");
                }

                // Actualizar codigo de respuesta a 999
                pstmt = con.prepareStatement(UPDATE_CANCELA_QRY);
                pstmt.setString(1, idReceived);
                if(pstmt.executeUpdate() < 1) {
                    throw new JMSException("ERROR: No se actualizo ningun pago con idAutorizacion = " + idReceived);
                }

                // Actualizar tarjeta reintegrando el importe
                pstmt = con.prepareStatement(UPDATE_TARJETA_QRY);
                pstmt.setDouble(1, importe);
                pstmt.setString(2, numTarjeta);
                if(pstmt.executeUpdate() < 1) {
                    throw new JMSException("ERROR: No se actualizo ningun pago con idAutorizacion = " + idReceived);
                }

                /*****************************/
            } else {
                logger.warning(
                        "Message of wrong type: "
                        + inMessage.getClass().getName());
            }
        } catch (JMSException e) {
            e.printStackTrace();
            mdc.setRollbackOnly();
        } catch (Throwable te) {
            te.printStackTrace();
        } finally {
            /*******************/
            try {
                if (rs != null) {
                    rs.close(); rs = null;
                }
                if (pstmt != null) {
                    pstmt.close(); pstmt = null;
                }
                if (con != null) {
                    closeConnection(con); con = null;
                }
            } catch (SQLException e) {

            }
            /*******************/
        }
  }


}
