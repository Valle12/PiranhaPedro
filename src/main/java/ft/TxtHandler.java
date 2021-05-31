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
      readPiranhaPrompt,
      readPiranha;
  private int dirCardOne, dirCardTwo, distCardOne, distCardTwo, x, y;
  private Frame frame;

  public TxtHandler(String path, Frame frame, String name) {
    this.path = path;
    this.frame = frame;
    this.name = name;
  }

  public void writePlayCards(int dirCardOne, int distCardOne, int dirCardTwo, int distCardTwo) {
    this.writePlayCards = true;
    this.dirCardOne = dirCardOne;
    this.distCardOne = distCardOne;
    this.dirCardTwo = dirCardTwo;
    this.distCardTwo = distCardTwo;
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
      bw.write("R: " + dirCardOne + ", S: " + distCardOne);
      if ((dirCardTwo != -1) && (distCardTwo != -1)) {
        bw.write("\nR: " + dirCardTwo + ", S: " + distCardTwo);
      }
      bw.flush();
      bw.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void writePiranhaPrompt(boolean currentCard) {
    this.writePiranhaPrompt = true;
    this.currentCard = currentCard;
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
    this.writePiranha = true;
    this.y = y;
    this.x = x;
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
      bw.write(y + ", " + x);
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
        String[] parts = line.split(",");
        frame.setPlayCards(
            Integer.parseInt(String.valueOf(parts[0].charAt(3))),
            Integer.parseInt(String.valueOf(parts[1].charAt(4))),
            name);
      }
      br.close();
      bw = new BufferedWriter(new FileWriter(path, true));
      bw.write("#");
      bw.flush();
      bw.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void readPiranhaPrompt() {
    this.readPiranhaPrompt = true;
  }

  private void readPiranhaPromptIntern() {
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
        String[] parts = line.split(" ");
        frame.setPiranhaPrompt(parts[1].equals("true"));
      }
      br.close();
      bw = new BufferedWriter(new FileWriter(path, true));
      bw.write("#");
      bw.flush();
      bw.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void readPiranha() {
    this.readPiranha = true;
  }

  public void readPiranhaIntern() {
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
        String[] parts = line.split(", ");
        frame.setPiranha(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
      }
      br.close();
      bw = new BufferedWriter(new FileWriter(path, true));
      bw.write("#");
      bw.flush();
      bw.close();
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
      if (readPiranhaPrompt) {
        readPiranhaPromptIntern();
        readPiranhaPrompt = false;
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
