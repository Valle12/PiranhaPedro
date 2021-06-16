package net.client;

import game.Board;
import game.Frame;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class Client {
  private ClientProtocol connection;
  private static String ipAdr;
  private int id;
  private int port;
  private Frame frame;

  public Client(int id, Frame frame, int port) {
    this.id = id;
    this.frame = frame;
    this.port = port;

    try {
      ipAdr = getLocalHostLANAddress().getHostAddress();
    } catch (UnknownHostException e) {
      e.printStackTrace();
    }
  }

  /**
   * https://stackoverflow.com/questions/9481865/getting-the-ip-address-of-the-current-machine-using-java
   *
   * @return
   * @throws UnknownHostException
   */
  private static InetAddress getLocalHostLANAddress() throws UnknownHostException {
    try {
      InetAddress candidateAddress = null;
      // Iterate all NICs (network interface cards)...
      for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces();
          ifaces.hasMoreElements(); ) {
        NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
        // Iterate all IP addresses assigned to each card...
        for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements(); ) {
          InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
          if (!inetAddr.isLoopbackAddress()) {

            if (inetAddr.isSiteLocalAddress()) {
              // Found non-loopback site-local address. Return it immediately...
              return inetAddr;
            } else if (candidateAddress == null) {
              // Found non-loopback address, but not necessarily site-local.
              // Store it as a candidate to be returned if site-local address is not subsequently
              // found...
              candidateAddress = inetAddr;
              // Note that we don't repeatedly assign non-loopback non-site-local addresses as
              // candidates,
              // only the first. For subsequent iterations, candidate will be non-null.
            }
          }
        }
      }
      if (candidateAddress != null) {
        // We did not find a site-local address, but we found some other non-loopback address.
        // Server might have a non-site-local address assigned to its NIC (or it might be running
        // IPv6 which deprecates the "site-local" concept).
        // Return this non-loopback candidate address...
        return candidateAddress;
      }
      // At this point, we did not find a non-loopback address.
      // Fall back to returning whatever InetAddress.getLocalHost() returns...
      InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
      if (jdkSuppliedAddress == null) {
        throw new UnknownHostException(
            "The JDK InetAddress.getLocalHost() method unexpectedly returned null.");
      }
      return jdkSuppliedAddress;
    } catch (Exception e) {
      UnknownHostException unknownHostException =
          new UnknownHostException("Failed to determine LAN address: " + e);
      unknownHostException.initCause(e);
      throw unknownHostException;
    }
  }

  // Messages

  public void connect() {
    connection = new ClientProtocol(Client.ipAdr, this, port);
    connection.start();
  }

  public void connect(Board board) {
    connection = new ClientProtocol(Client.ipAdr, this, port, board);
    connection.start();
  }

  public void disconnect() {
    connection.disconnect();
  }

  public void sendMessage(String message) {
    connection.sendMessage(message);
  }

  public void updatePlayCards(int playCard, int index, boolean firstPlayer) {
    connection.updatePlayCards(playCard, index, firstPlayer);
  }

  public void updatePiranha(int index, int value, int i, int j) {
    connection.updatePiranha(index, value, i, j);
  }

  public void setAgent(int index, int value) {
    connection.setAgent(index, value);
  }

  public void setGameBoard(Board board) {
    frame.setGameBoard(board);
    frame.repaintBoard();
  }

  // Getter

  public int getID() {
    return id;
  }

  public Frame getFrame() {
    return frame;
  }
}
