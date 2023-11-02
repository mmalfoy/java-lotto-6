package lotto.domain;

import camp.nextstep.edu.missionutils.Console;

public class InputUI {
    private final int ERR_NUM = Integer.MIN_VALUE;
    private int bonusNum;
    private int cost;

    public void purchase() {
        try {
            System.out.println("구입금액을 입력해 주세요.");
            String tempCost = Console.readLine();
            cost = checkValidPurchase(tempCost);
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    public int checkValidPurchase(String tempCost) {
        try {
            int cost = Integer.parseInt(tempCost);
            if (cost < 0) {
                throw new IllegalArgumentException("[ERROR] 구입 금액은 0원 이상이여야 합니다.");
            }
            if (cost % 1000 != 0) {
                throw new IllegalArgumentException("[ERROR] 구입 금액은 1000원 단위여야 합니다.");
            }
            return cost;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("[ERROR] 구입 금액은 정수여야 합니다.");
        }
    }

    public void bonusBall() {
        try {
            System.out.println("보너스 번호를 입력해 주세요.");
            String tempBonus = Console.readLine();
            bonusNum = checkValidBonusNum(tempBonus);
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    public int checkValidBonusNum(String tempBonus) {
        try {
            int bonusNum = Integer.parseInt(tempBonus);
            if (bonusNum < 0 || bonusNum > 45) {
                throw new IllegalArgumentException("[ERROR] 보너스 번호는 1부터 45 사이의 숫자여야 합니다.");
            }
            return bonusNum;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("[ERROR] 보너스 번호는 정수여야 합니다.");
        }
    }


}
