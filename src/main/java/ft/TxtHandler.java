package ft;

import game.Frame;

import java.io.*;

public class TxtHandler extends Thread {
  private String path, name;
  private BufferedReader br;
  private BufferedWriter bw;
  private boolean running,
      writePlayCards,
      writePiranhaPrompt,
      currentCard,
      writePiranha,
      readPlayCards,
      readPiranha,
      config,
      writeConfig;
  private int dirCardOne, dirCardTwo, distCardOne, distCardTwo, x, y;
  private Frame frame;

  public TxtHandler(String path, Frame frame, String name) {
    this.path = path;
    this.frame = frame;
    this.name = name;
  }

  public void writePlayCards(int dirCardOne, int distCardOne, int dirCardTwo, int distCardTwo) {
    this.dirCardOne = dirCardOne;
    this.distCardOne = distCardOne;
    this.dirCardTwo = dirCardTwo;
    this.distCardTwo = distCardTwo;
    this.writePlayCards = true;
  }

  private void writePlayCards() {
    while (!isHashtagAvailable()) {
      try {
        sleep(100);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    try {
      bw = new BufferedWriter(new FileWriter(path));
      bw.write("(R:" + dirCardOne + ", S:" + distCardOne + ")");
      if ((dirCardTwo != -1) && (distCardTwo != -1)) {
        bw.write("\n(R:" + dirCardTwo + ", S:" + distCardTwo + ")");
      }
      bw.flush();
      bw.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void writePiranhaPrompt(boolean currentCard) {
    this.currentCard = currentCard;
    this.writePiranhaPrompt = true;
  }

  private void writePiranhaPrompt() {
    while (!isHashtagAvailable()) {
      try {
        sleep(100);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    try {
      bw = new BufferedWriter(new FileWriter(path));
      bw.write("PIRANHA " + currentCard);
      bw.flush();
      bw.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void writePiranha(int y, int x) {
    this.y = y;
    this.x = x;
    this.writePiranha = true;
  }

  private void writePiranha() {
    while (!isHashtagAvailable()) {
      try {
        sleep(100);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    try {
      bw = new BufferedWriter(new FileWriter(path));
      bw.write("(" + y + ", " + x + ")\n");
      System.out.println("TXTHANDLER: Write Piranha at x: " + x + ", y: " + y);
      bw.flush();
      bw.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void writeConfig(boolean config) {
    this.config = config;
    this.writeConfig = true;
  }

  private void writeConfigIntern() {
    while (!isHashtagAvailable()) {
      try {
        sleep(100);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    try {
      bw = new BufferedWriter(new FileWriter(path));
      bw.write(config + "");
      bw.flush();
      bw.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void readPlayCards() {
    this.readPlayCards = true;
  }

  private void readPlayCardsIntern() {
    String line;
    String tmpLine = null;
    while (isHashtagAvailable()) {
      try {
        sleep(100);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    try {
      br = new BufferedReader(new FileReader(path));
      while ((line = br.readLine()) != null) {
        if (!line.matches("\\(R:\\d, S:\\d\\)")) {
          return;
        }
        System.out.println("TXTHANDLER: Card read: " + line);
        String[] parts = line.split(",");
        frame.setPlayCards(
            Integer.parseInt(String.valueOf(parts[0].charAt(3))),
            Integer.parseInt(String.valueOf(parts[1].charAt(3))),
            name);
        tmpLine = line;
      }
      if (tmpLine != null) {
        br.close();
        bw = new BufferedWriter(new FileWriter(path, true));
        bw.write("\n#");
        bw.flush();
        bw.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void readPiranha() {
    this.readPiranha = true;
  }

  private void readPiranhaIntern() {
    String line;
    while (isHashtagAvailable()) {
      try {
        sleep(100);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    try {
      br = new BufferedReader(new FileReader(path));
      while ((line = br.readLine()) != null) {
        if (line.matches("\\(\\d+, \\d+\\)")) {
          System.out.println("TXTHANDLER: Read Piranha");
          String[] parts = line.split(", ");
          frame.setPiranha(
              Integer.parseInt(parts[1].substring(0, parts[1].length() - 1)),
              Integer.parseInt(parts[0].substring(1)));
          bw = new BufferedWriter(new FileWriter(path, true));
          bw.write("\n#");
          bw.flush();
          bw.close();
        }
      }
      br.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private boolean isHashtagAvailable() {
    String line;
    try {
      br = new BufferedReader(new FileReader(path));
      while ((line = br.readLine()) != null) {
        if (line.equals("#")) {
          return true;
        }
      }
      br.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return false;
  }

  @Override
  public void run() {
    running = true;
    while (running) {
      if (writeConfig) {
        writeConfigIntern();
        writeConfig = false;
      }
      if (writePlayCards) {
        writePlayCards();
        writePlayCards = false;
      }
      if (writePiranhaPrompt) {
        writePiranhaPrompt();
        writePiranhaPrompt = false;
      }
      if (writePiranha) {
        writePiranha();
        writePiranha = false;
      }
      if (readPlayCards) {
        readPlayCardsIntern();
        readPlayCards = false;
      }
      if (readPiranha) {
        readPiranhaIntern();
        readPiranha = false;
      }
      try {
        sleep(1);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
