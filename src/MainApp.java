import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Arman Tapayev
 */
public class MainApp {
    private static BufferedReader br;                   // Читаем данные из консоли
    private static FantasyCharacter player = null;      // Герой должен хранится на протяжении всей игры
    private static Seller merchant = null;
    private static BattleScene battleScene = null;      // Класс для битвы

    public static void main(String[] args) {
        br = new BufferedReader(new InputStreamReader(System.in));
        battleScene = new BattleScene();
        System.out.println("Введите имя персонажа:");   // Ввод имени персонажа
        // Ожидание ввода пользователя
        try {
            command(br.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void command(String string) throws IOException {
        // Если это первый запуск, то создаем игрока
        if (player == null) {
            player = new Hero(string, 40, 20, 20, 0, 100);
            System.out.printf("Имя Героя - %s!%n", player.getName());
            printNavigation();  // Вывод меню
        }

        if (merchant == null) {
            merchant = new Merchant();
        }

        switch (string) {
            case "1": {
                System.out.println("Торговец слушает! Стоимость зелья - 10 монет за 20 здоровья! (Купить/Отказаться)");
                command(br.readLine());
            }
            break;

            case "2": {
                commitFight();
            }
            break;

            case "3":
                System.exit(1);
                break;

            case "да":
                command("2");
                break;

            case "нет": {
                printNavigation();
                command(br.readLine());
            }

            case "Купить": {
                System.out.println(merchant.sell(Merchant.Goods.POTION));
                player.setHealthPoints(player.getHealthPoints() + 20);
                player.setGold(player.getGold() - 10);
                System.out.println(player);
                System.out.println(merchant);
                printNavigation();
                command(br.readLine());
            }
            break;

            case "Отказаться": {
                System.out.println("Может желаете пройти вакцинацию? Нет? Очень жаль...");
                printNavigation();
                command(br.readLine());
            }
        }
        // Ждем очередной команды от пользователя
        command(br.readLine());
    }

    private static void commitFight() {
        battleScene.fight(player, createMonster(), new FightCallback() {
            @Override
            public void fightWin() {
                System.out.printf("%s победил! Опыт: %d, золото: %d, здоровье: %d.%n", player.getName(), player.getXp(), player.getGold(), player.getHealthPoints());
                System.out.println("Желаете продолжить поход или вернуться в город? (да/нет)");
                try {
                    command(br.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fightLost() {
                System.out.printf("%s погиб! %n", player.getName());
                System.out.println("Желаете купить зелье (1) или выйти из игры (3)?");
                try {
                    command(br.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static void printNavigation() {
        System.out.println("Куда Вы хотите пойти?");
        System.out.println("1. К Торговцу");
        System.out.println("2. В темный лес");
        System.out.println("3. Выход");
    }

    private static FantasyCharacter createMonster() {
        // С вероятностью 50% создаем скелета или гоблина
        int random = (int) (Math.random() * 10);
        if (random > 5)
            return new Goblin("Гоблин", 70, 10, 10, 10, 20);
        else
            return new Skeleton("Скелет", 40, 20, 20, 5, 10);
    }

    interface FightCallback {
        void fightWin();

        void fightLost();
    }
}
