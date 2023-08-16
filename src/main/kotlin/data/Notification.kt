package data

import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.util.*
import javax.swing.JOptionPane


class Notificacion {
    var popup = PopupMenu()
    private val image: Image = Toolkit.getDefaultToolkit().getImage("src/main/resources/images/notification_ring.png")
    private val trayIcon = TrayIcon(image, "Aplicación Java", popup)

    //obtiene instancia SystemTray
    val systemtray = SystemTray.getSystemTray()

    //para el Timer
    private var timer: Timer? = null

    init {
        //comprueba si SystemTray es soportado en el sistema
        if (SystemTray.isSupported()) {

            //acciones del raton sobre el icono en la barra de tareas
            val mouseListener: MouseListener = object : MouseListener {
                override fun mouseClicked(evt: MouseEvent) {
                    //Si se presiono el boton izquierdo y la aplicacion esta minimizada
                    if (evt.getButton() === MouseEvent.BUTTON1) // && miframe.getExtendedState()==JFrame.ICONIFIED )
                    {
                        MensajeTrayIcon(
                            "hola chris",
                            "Por favor verifique la información",
                            TrayIcon.MessageType.WARNING
                        )
                    }
                }

                override fun mouseEntered(evt: MouseEvent?) {}
                override fun mouseExited(evt: MouseEvent?) {}
                override fun mousePressed(evt: MouseEvent?) {}
                override fun mouseReleased(evt: MouseEvent?) {}
            }

            //ACCIONES DEL MENU POPUP
            val salir = ActionListener { e: ActionEvent? ->
                System.exit(0);
                systemtray.remove(trayIcon);
                band = true
            }
            val verinformacion = ActionListener { e: ActionEvent? ->
                JOptionPane.showMessageDialog(
                    null, "Imprimendo ", "Aplicación Java",
                    JOptionPane.INFORMATION_MESSAGE
                )
                band = true
            }
            //Se crean los Items del menu PopUp y se añaden
            val SalirItem = MenuItem("Salir")
            SalirItem.addActionListener(salir)
            popup.add(SalirItem)
            val Itemverinfo = MenuItem("Ver informacion")
            Itemverinfo.addActionListener(verinformacion)
            popup.add(Itemverinfo)
            trayIcon.setImageAutoSize(true)
            trayIcon.addMouseListener(mouseListener)

            //Añade el TrayIcon al SystemTray
            try {
                systemtray.add(trayIcon)
            } catch (e: AWTException) {
                System.err.println("Error:" + e.message)
            }
        } else {
            System.err.println("Error: SystemTray no es soportado")
        }
    }

    //Muestra una burbuja con la accion que se realiza
    fun MensajeTrayIcon(title: String?, texto: String?, tipo: TrayIcon.MessageType = TrayIcon.MessageType.NONE) {
        trayIcon.displayMessage("Notificación Sistema Java:", texto, tipo)
    }

    companion object {
        var band = false
    }
}

