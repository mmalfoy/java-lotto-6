package lotto.domain;

import camp.nextstep.edu.missionutils.Randoms;

import java.util.*;
import java.util.stream.Collectors;

import lotto.UI.UserView;
import lotto.data.Lotto;
import lotto.data.Rewards;

public class LottoModel {

    private boolean hasBonus;
    private final int CHECK_BONUS = 999;
    private final InputUI inputUI;
    private final UserView userView;
    private List<Lotto> publishedLottos;
    private int totalEarnedMoney;
    private HashMap<Rewards, Integer> winningTable;
    private HashMap<Integer, Integer> winningNumsTable;


    public LottoModel() {
        this.inputUI = new InputUI();
        this.userView = new UserView();
        this.publishedLottos = new ArrayList<>();
        this.winningTable = initWinningTable();
        this.totalEarnedMoney = 0;
    }

    public HashMap<Integer, Integer> compareLotto(List<Integer> winnings, Lotto lotto, int bonusNum) {
        HashMap<Integer, Integer> winningNumsTable = initWinningNumsTable(winnings);
        for (int number : lotto.getNumbers()) {
            if (winningNumsTable.containsKey(number)) {
                winningNumsTable.put(number, 1);
            }
            if (number == bonusNum) {
                winningNumsTable.put(CHECK_BONUS, 1);
            }
        }
        return winningNumsTable;
    }

    public void computeLotto(List<Lotto> published, List<Integer> winnings, int bonusNum) {

        for (Lotto lotto : published) {
            hasBonus = false;
            winningNumsTable = compareLotto(winnings, lotto, bonusNum);
            int result = sumOfWinningNumsTable();
            winningTable = makeWinningTable(result, hasBonus);
        }

    }

    public String computeRate(int totalEarned, int purchased) {
        double winningRate = (double) totalEarned / purchased * 100;
        double roundedRate = Math.round(winningRate * 10.0) / 10.0;
        return String.format("%.1f", roundedRate);
    }

    public List<Integer> generateNewLotto() {
        List<Integer> generated = new ArrayList<>(
                Randoms.pickUniqueNumbersInRange(1, 45, 6));
        Collections.sort(generated);
        return generated;
    }

    public HashMap<Integer, Integer> initWinningNumsTable(List<Integer> winnings) {
        HashMap<Integer, Integer> winningNums = new HashMap<>();
        for (int winningnum : winnings) {
            winningNums.put(winningnum, 0);
        }
        return winningNums;
    }

    public HashMap<Rewards, Integer> initWinningTable() {
        HashMap<Rewards, Integer> winningTable = new HashMap<>();
        Rewards[] rewards = Rewards.values();
        for (Rewards reward : rewards) {
            winningTable.put(reward, 0);
        }
        return winningTable;
    }

    public HashMap<Rewards, Integer> makeWinningTable(int result, boolean bonus) {
        Rewards[] rewards = Rewards.values();
        for (Rewards reward : rewards) {
            if (result == reward.correctLottos() && bonus == reward.correctBonus()) {
                totalEarnedMoney += reward.money();
                int updateNum = winningTable.getOrDefault(reward, 0) + 1;
                winningTable.put(reward, updateNum);
            }
        }
        return winningTable;
    }

    public void publishLotto(int numOfLotto) {
        List<Integer> randNums;
        try {
            for (int i = 0; i < numOfLotto; i++) {
                randNums = generateNewLotto();
                publishedLottos.add(new Lotto(randNums));
            }
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    public String publishTicket(List<Integer> lottoNums) {
        lottoNums.sort(Comparator.naturalOrder());
        List<String> sortedNums = lottoNums.stream()
                .map(String::valueOf)
                .collect(Collectors.toList());
        String Ticket = String.format("[%s]", String.join(", ", sortedNums));
        return Ticket;
    }

    public void run() {
        inputUI.purchase();
        publishLotto(inputUI.getNumofLotto());
        userView.purchaseLog(inputUI.getNumofLotto(), this.publishedLottos);
        inputUI.winnings();
        inputUI.bonusBall();
        computeLotto(publishedLottos, inputUI.getWinningNums(), inputUI.getBonusNum());
        userView.winnings(winningTable);
        userView.rate(computeRate(totalEarnedMoney, inputUI.getCost()));
    }

    public int sumOfWinningNumsTable() {
        int result = 0;
        for (int value : winningNumsTable.values()) {
            result += value;
        }
        return result;
    }


}
