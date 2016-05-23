/*
Handles all the math operations
Utilied by all the classes!
Authors : Dhrumil Patel, Kena Patel
*/


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LargeInteger {

    List<Integer> values;
    boolean negative;

    public LargeInteger() {
        negative = false;
        values = new ArrayList<>();
    }

    public LargeInteger(String str) {
        values = new ArrayList<>();
        negative = str.startsWith("-1");
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {
                values.add(str.charAt(i) - '0');
            }
        }
        Collections.reverse(values);
    }

    public LargeInteger(List<Integer> values, boolean negative) {
        this.values = values;
        this.negative = negative;
    }

    public LargeInteger(int n) {
        negative = false;
        if (n < 0) {
            negative = true;
            n = -n;
        }
        values = new ArrayList<>();
        while (n > 0) {
            int rem = n % 10;
            n /= 10;
            values.add(rem);
        }
    }

    public LargeInteger(LargeInteger n) {
        values = new ArrayList<>();
        for (int v : n.getValues()) {
            values.add(v);
        }
        negative = n.isNegative();
    }

    static LargeInteger divideBy2(LargeInteger a) {

        List<Integer> newList = new ArrayList<>();
        int last = 0;
        boolean start = false;
        for (int i = a.getValues().size() - 1; i >= 0; i--) {

            if (!start) {
                if (a.getValues().get(i) == 0)
                    continue;
            }
            start = true;
            int tmp = a.getValues().get(i) + 10 * last;
            int v = tmp / 2;

            newList.add(v);
            last = tmp % 2;
        }
        Collections.reverse(newList);
        trimLeadingZeros(newList);

        return new LargeInteger(newList, a.isNegative());
    }

    private static void trimLeadingZeros(List<Integer> newList) {
        while (newList.size() > 0) {
            if (newList.get(newList.size() - 1) == 0)
                newList.remove(newList.size() - 1);
            else
                break;
        }
    }

    private static int absCompare(LargeInteger a, LargeInteger b) {
        for (int i = Math.max(a.getValues().size(), b.getValues().size()) - 1; i >= 0; i--) {
            int t1 = ((i < a.getValues().size()) ? a.getValues().get(i) : 0);
            int t2 = ((i < b.getValues().size()) ? b.getValues().get(i) : 0);
            if (t1 < t2)
                return -1;
            else if (t2 < t1)
                return 1;

        }
        return 0;
    }

    public static int compare(LargeInteger a, LargeInteger b) {
        if (!a.isNegative() && !b.isNegative())
            return absCompare(a, b);
        else if (a.isNegative() && b.isNegative()) {
            return -absCompare(a, b);
        } else if (a.isNegative())
            return -1;
        else if (b.isNegative())
            return 1;

        return 0;
    }

    public static void main(String args[]) {
        System.out.println(new LargeInteger(128).power(5));
//        System.out.println(RSA.randomNumber(300).multiply(RSA.randomNumber(300)));
//        System.out.println(RSA.randomNumber(500).divide(RSA.randomNumber(20)));
        long start = System.currentTimeMillis();
        System.out.println(RSA.modPow(RSA.randomNumber(100), RSA.randomNumber(100), RSA.randomNumber(100)));

        long end = System.currentTimeMillis();
        System.out.println("Time taken: " + (end - start) + "  ms");
//
//        LargeInteger integer = new LargeInteger(32921);
//        System.out.println(integer);
//        integer.subtract(new LargeInteger(13929000));
//        System.out.println(integer);
//
//        Random random = new Random();
//        for (int i = 0; i < 10; i++) {
//
//            int a = random.nextInt(10000);
//            int b = random.nextInt(10000);
//            LargeInteger aa = new LargeInteger(a);
//            LargeInteger bb = new LargeInteger(b);
//            int aminusb = a - b;
//            int aplusb = a + b;
//            LargeInteger sub = new LargeInteger(aa);
//            sub.subtract(bb);
//            LargeInteger add = new LargeInteger(aa);
//            add.add(bb);
//            System.out.println(a + " - " + b + " =  " + sub + ", " + aminusb);
//            System.out.println(a + " + " + b + " =  " + add + ", " + aplusb);
//
//        }
//
//        for (int i = 0; i < 10; i++) {
//
//            int a = random.nextInt(10000);
//            int b = random.nextInt(10000);
//            if (random.nextBoolean())
//                a = -a;
//            if (random.nextBoolean())
//                b = -b;
//            LargeInteger aa = new LargeInteger(a);
//            LargeInteger bb = new LargeInteger(b);
//            int aminusb = a * b;
//
//            LargeInteger add = new LargeInteger(aa);
//            add.multiply(bb);
//            System.out.println(a + " * " + b + " =  " + add + ", " + aminusb);
//
//        }
//
//        for (int i = 0; i < 10; i++) {
//
//            int a = random.nextInt(10000);
//            int b = random.nextInt(100);
//            if (random.nextBoolean())
//                a = -a;
//            if (random.nextBoolean())
//                b = -b;
//            LargeInteger aa = new LargeInteger(a);
//            LargeInteger bb = new LargeInteger(b);
//            int aminusb = a / b;
//
//            LargeInteger add = new LargeInteger(aa);
//            add.divide(bb);
//            System.out.println(a + " / " + b + " =  " + add + ", " + aminusb);
//
//        }
//        System.out.println(new LargeInteger(2).power(100));
//        System.out.println(new LargeInteger(32828).mod(new LargeInteger(19)));
//        System.out.println(new LargeInteger(100).mod(new LargeInteger(2)));


    }

    public List<Integer> getValues() {
        return values;
    }

    public boolean isNegative() {
        return negative;
    }

    public void setNegative(boolean negative) {
        this.negative = negative;
    }

    void add(LargeInteger from) {
        if (!from.isNegative() && !isNegative()) {
            List<Integer> fromList = from.getValues();
            int i = 0;
            int rem = 0;
            List<Integer> newList = new ArrayList<>();
            for (i = 0; i < fromList.size() && i < values.size(); i++) {
                int a = fromList.get(i) + values.get(i) + rem;
                newList.add(a % 10);
                rem = a / 10;
            }
            for (; i < fromList.size(); i++) {
                int a = fromList.get(i) + rem;
                newList.add(a % 10);
                rem = a / 10;
            }
            for (; i < values.size(); i++) {
                int a = values.get(i) + rem;
                newList.add(a % 10);
                rem = a / 10;
            }
            if (rem > 0) {
                newList.add(rem);
            }
            values = newList;
        } else if (from.isNegative() && isNegative()) {
            from.setNegative(false);
            setNegative(false);
            add(from);
            setNegative(true);
        } else if (absCompare(from, this) == 0) {
            values = new ArrayList<>();
            negative = false;
        } else if (isNegative()) {
            if (absCompare(this, from) > 0) { // negative result
                LargeInteger t = subHelper(this, from);
                values = t.getValues();
                negative = true;
            } else {
                LargeInteger t = subHelper(from, this);
                values = t.getValues();
                negative = false;
            }
        } else {
            if (absCompare(this, from) > 0) {
                LargeInteger t = subHelper(this, from);
                values = t.getValues();
                negative = false;
            } else {
                LargeInteger t = subHelper(from, this);
                values = t.getValues();
                negative = true;
            }
        }
    }

    LargeInteger multiply(LargeInteger a) {

        if (a.getValues().size() == 0 || getValues().size() == 0) {
            setNegative(false);
            values = new ArrayList<>();
        }
        LargeInteger result = new LargeInteger();
        for (int i = 0; i < a.getValues().size(); i++) {
            ArrayList<Integer> tmp = new ArrayList<>();
            int rem = 0;
            for (int j = 0; j < values.size(); j++) {
                rem = a.getValues().get(i) * getValues().get(j) + rem;
                tmp.add(rem % 10);
                rem /= 10;
            }
            tmp.add(rem);
            for (int j = 0; j < i; j++) {
                tmp.add(0, 0);
            }
            result.add(new LargeInteger(tmp, false));
        }
        values = result.getValues();
        setNegative(negative != a.isNegative());
        return this;
    }

    LargeInteger modMultiply(LargeInteger a, LargeInteger mod) {

        if (a.getValues().size() == 0 || getValues().size() == 0) {
            setNegative(false);
            values = new ArrayList<>();
        }
        LargeInteger result = new LargeInteger();
        for (int i = 0; i < a.getValues().size(); i++) {
            ArrayList<Integer> tmp = new ArrayList<>();
            int rem = 0;
            for (int j = 0; j < values.size(); j++) {
                rem = a.getValues().get(i) * getValues().get(j) + rem;
                tmp.add(rem % 10);
                rem /= 10;
            }
            tmp.add(rem);
            for (int j = 0; j < i; j++) {
                tmp.add(0, 0);
            }
            LargeInteger t = new LargeInteger(tmp, false);
            t = t.mod(mod);
            result.add(t);
            result = result.mod(mod);
        }
        values = result.getValues();
        setNegative(negative != a.isNegative());
        return this;
    }

    LargeInteger divide(LargeInteger a) {
        if (absCompare(a, this) > 0) {
            values = new ArrayList<>();
            negative = false;
        } else if (absCompare(a, this) == 0) {
            values = new ArrayList<>();
            values.add(1);
            negative = isNegative() != a.isNegative();
        } else {
//            System.out.println("got here");
            negative = isNegative() != a.isNegative();

            LargeInteger lo = new LargeInteger(1);
            LargeInteger hi = new LargeInteger(this);
            hi.setNegative(false);
            lo.setNegative(false);
            while (absCompare(hi, lo) > 0) {
                LargeInteger tmp = new LargeInteger(hi);
                tmp.add(lo);
                LargeInteger mid = divideBy2(tmp);
                tmp = new LargeInteger(mid);
                tmp.multiply(a);

                if (absCompare(tmp, this) <= 0) {
                    LargeInteger tmp2 = new LargeInteger(mid);
                    tmp2.add(new LargeInteger(1));
                    tmp2.multiply(a);
                    if (absCompare(tmp2, this) > 0) {
                        values = mid.getValues();
                        return this;
                    }
                }

                if (absCompare(tmp, this) > 0) {
                    hi = mid;

                } else {
                    lo = mid;
                    lo.add(new LargeInteger(1));

                }
            }
        }
        return this;
    }

    public int toInt() {
        return Integer.parseInt(toString());
    }

    LargeInteger mod(LargeInteger a) {
        LargeInteger temp = new LargeInteger(this);
        temp.divide(a);
        LargeInteger mul = new LargeInteger(a);
        mul.multiply(temp);
        LargeInteger mod = new LargeInteger(this);
        mod.subtract(mul);
        return mod;
    }

    public boolean isEven() {
        return values.size() == 0 || values.get(0) % 2 == 0;
    }

    public LargeInteger subtract(LargeInteger a) {
        LargeInteger t = new LargeInteger(a);
        t.setNegative(!a.isNegative());
        add(t);
        return this;
    }

    private LargeInteger subHelper(LargeInteger a, LargeInteger b) {
        int borrow = 0;
        List<Integer> newList = new ArrayList<>();
        for (int i = 0; i < Math.max(a.getValues().size(), b.getValues().size()); i++) {
            int result = ((i < a.values.size()) ? a.values.get(i) : 0) -
                    ((i < b.getValues().size()) ? b.getValues().get(i) : 0) - borrow;
            if (result < 0) {
                newList.add(result + 10);
                borrow = 1;
            } else {
                newList.add(result);
                borrow = 0;
            }
        }
        return new LargeInteger(newList, borrow > 0);
    }

    @Override
    public String toString() {
        if (values.size() == 0) {
            return "0";
        }
        trimLeadingZeros(values);
        String ret = "";
        if (negative)
            ret = "-";
        for (int i = values.size() - 1; i >= 0; i--)
            ret += values.get(i);
        return ret;
    }

    LargeInteger power(int p) {
        LargeInteger ret = new LargeInteger(1);
        LargeInteger n = new LargeInteger(this);
        while (p > 0) {
            if (p % 2 == 1) {
                ret.multiply(n);
            }

            p = p / 2;
            n.multiply(n);
        }
        trimLeadingZeros(ret.getValues());
        return ret;

    }
}
