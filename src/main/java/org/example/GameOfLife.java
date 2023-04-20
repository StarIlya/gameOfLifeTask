package org.example;

import java.io.*;

public class GameOfLife {

    private char[][] playField; // out play field
    private int fieldLengthX; // field length by X coordinate(columns count)
    private int fieldLengthY; // field length by X coordinate(rows count)
    private int getCount; //generation count
    private static final String OUTPUT_PATH = "target/test-classes/";
    private static final String INPUT_PATH = "src/test/resources";
    private static final char ALIVE_CELL = 'X';
    private static final char DEAD_CELL = 'Y';

    public static void main(String[] args) {
        GameOfLife game = new GameOfLife();
        game.game(INPUT_PATH, OUTPUT_PATH);

    }
    public void game(String fileNameInput, String fileNameOutput) {
        readFile(INPUT_PATH + fileNameInput);
        run();
        writeFile(OUTPUT_PATH + fileNameOutput);
    }

    private void run() {
        char[][] newPlayField;
        for (int i = 0; i < getCount; i++) {  // generation count loop
            newPlayField = new char[fieldLengthY][fieldLengthX];
            for (int y = 0; y < fieldLengthY; y++) { // row loop
                for (int x = 0; x < fieldLengthX; x++) { // column loop
                    newPlayField[y][x] = getCellNextGen(y, x);
                }
            }
            playField = newPlayField;
        }
    }

    private void readFile(String fileNameInput) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileNameInput))) {
            String setting = reader.readLine();
            setSettings(splitSettings(setting));
            for (int y = 0; y < fieldLengthY; y++) {
                for (int x = 0; x < fieldLengthX; x++) {
                    playField[y][x] = (char) reader.read();
                    reader.read();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeFile(String fileNameOutput) {
        StringBuilder data = new StringBuilder();
        for (int y = 0; y < fieldLengthY; y++) {
            for (int x = 0; x < fieldLengthX; x++) {
                data.append(playField[y][x]).append(' ');
            }
            data.setLength(data.length() - 1);
            data.append('\n');
        }

        data.setLength(data.length() - 1);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileNameOutput))) {
            writer.write(data.toString());
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private char getCellNextGen(int y, int x) {
        int neighboursCount = 0;
        int row;
        int col;

        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                row = (y + i + fieldLengthY) % fieldLengthY;
                col = (x + j + fieldLengthX) % fieldLengthX;
                if (playField[row][col] == ALIVE_CELL) {
                    neighboursCount++;
                }
            }
        }

        if (playField[y][x] == ALIVE_CELL) {
            neighboursCount--;
            return  getCellByState(shouldAliveCellLive(neighboursCount));
        }
        else {
            return getCellByState(shouldDeadCellLive(neighboursCount));
        }
    }

    private boolean shouldAliveCellLive(int neighboursCount) {
        return neighboursCount > 1 && neighboursCount < 4;
    }

    private boolean shouldDeadCellLive(int neighboursCount) {
        return neighboursCount == 3;
    }

    private char getCellByState(boolean state) {
        return state ? GameOfLife.ALIVE_CELL : GameOfLife.DEAD_CELL;
    }

    private void setSettings(int[] settings) {
        fieldLengthY = settings[0];
        fieldLengthX = settings[1];
        getCount = settings[2];
        playField = new char[fieldLengthY][fieldLengthX];
    }

    private int[] splitSettings(String settings) {
        int[] array = new int[3];
        int begin = 0;
        int index = 0;
        for (int i = 0; i < settings.length(); i++) {
            if (settings.charAt(i) == ',') {
                array[index++] = Integer.parseInt(settings.substring(begin, i));
                begin = i + 1;
            }
        }
        array[index] = Integer.parseInt(settings.substring(begin));
        return array;
    }
}
