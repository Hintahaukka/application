package hifian.hintahaukka.Service;



public class BarCodeChecker {

    public static void BarCodeChecker() {

    }

    public boolean checkEan13(String scanResult) {
        if (scanResult.length()!=13) return false;
        int odd=0;
        int even=0;
        int num;
        for (int i=0;i<12;i++) {
            num = Integer.parseInt(scanResult.substring(i, i+1));
            if (i%2==0) {
                odd += num;
            } else {
                even += num;
            }
        }
        int sum = even*3 + odd;
        int nextTen = (sum/10 + 1)*10;
        return  (nextTen-sum)==Integer.parseInt(scanResult.substring(12,13));

    }
}
