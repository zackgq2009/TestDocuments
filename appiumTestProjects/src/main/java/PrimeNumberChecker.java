/**
 * Created by johnny on 2017/7/13.
 */
//求质数
public class PrimeNumberChecker {
    public Boolean validate(final Integer primeNumber) {
        for (int i=2; i< (primeNumber / 2); i++) {
            if (primeNumber % i == 0) {
                return false;
            }
        }
        return  true;
    }
}
