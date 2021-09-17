/**
 * Created by Arman Tapayev
 */
public class Merchant implements Seller {
    int potionAmount = 1000000; // Склад лекарств "Крез и Ко" )))))
    int goldAmount = 0;

    @Override
    public String sell(Goods goods) {
        String result = "";
        if (goods == Goods.POTION) {
            potionAmount -= 20;
            goldAmount += 10;
            result = "Спасибо за покупку!";
        }
        return result;
    }

    @Override
    public String toString() {
        return String.format("У продавца осталось %d лекарств и %d золота", potionAmount, goldAmount);
    }

    public enum Goods {
        POTION
    }
}
