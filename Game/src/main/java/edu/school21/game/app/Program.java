package edu.school21.game.app;

import com.beust.jcommander.JCommander;
import edu.school21.chase.logic.Enemy;
import edu.school21.chase.logic.GameField;
import edu.school21.chase.logic.Player;
import edu.school21.game.logic.Parameters;
import edu.school21.game.logic.Parser;

import java.util.Scanner;

public class Program {
    public static void main(String[] args) {
        Parser jArgs = new Parser();
        JCommander argsJC = JCommander.newBuilder().addObject(jArgs).build();
        argsJC.parse(args);
        Parameters parameters = new Parameters(jArgs.profile);
        GameField gameField = new GameField(jArgs.sizeField, jArgs.wallsCount, jArgs.enemiesCount);
        Player player = new Player(gameField);
        Enemy[] enemies = new Enemy[jArgs.enemiesCount];
        for (int i = 0; i < jArgs.enemiesCount; i++) {
            enemies[i] = new Enemy(gameField.getEnemiesCoord()[i][0], gameField.getEnemiesCoord()[i][1], gameField, player);
        }
        Render render = new Render(gameField, jArgs.sizeField, parameters);
        render.Rendering();
        Scanner in = new Scanner(System.in);
        int movement = 0, checkWin = 0;
        while (checkWin == 0 && movement != 9) {
            do {
                movement = userIn(in);
                if (!parameters.isDebug() && movement == 8) {
                    movement = 0;
                }
                checkWin = player.movePlayer(movement);
                if (checkWin == -1) {
                    render.Rendering();
                }
            } while (checkWin == -1);
            if (checkWin != 0) {
                break;
            }
            for (int i = 0; i < jArgs.enemiesCount; i++) {
                if (parameters.isDebug()) {
                    do {
                        render.Rendering();
                        System.out.printf("Enemy %d/%d (%d, %d): ", i + 1, jArgs.enemiesCount, gameField.getEnemiesCoord()[i][0], gameField.getEnemiesCoord()[i][1]);
                    } while (userIn(in) != 8);
                }
                if ((checkWin = enemies[i].enemyNextStep()) != 0) {
                    break;
                }
            }
            render.Rendering();
        }
        render.Rendering();
        if (checkWin == 2) {
            System.out.println("You are Winner!");
        } else {
            System.out.println("You LOh");
        }
    }

    public static int userIn(Scanner in) {
        int result = 0;
        if (in.hasNext() && in.hasNextInt()) {
            result = in.nextInt();
            if ((result < 1 || result > 3) && (result != 5) && (result != 9) && (result != 8)) {
                System.err.printf("zsh: command not found: %d\n", result);
                result = 0;
            }
        } else if (in.hasNextLine()) {
            System.err.printf("zsh: command not found: %s\n", in.next());
        }
        return result;
    }
}
