/**
 * Created by Tatiana on 03.07.2017.
 */
import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class apply_rule {

    public static void main(String args[]) throws FileNotFoundException, IOException, NoSuchAlgorithmException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream("test.rules")));
        BufferedReader pass = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream("SkullSecurityComp")));
       /* BufferedReader readercat = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream("no.txt")));*/
        List<String> cat = new ArrayList();
        String str;
        while ((str = reader.readLine()) != null) cat.add(str);
        long start = System.currentTimeMillis();
        String rule_test;
        String test, res;
       // System.out.println(apply_rules(test, rule_test));
         while((test=pass.readLine())!=null) {
             for (int i = 0; i < cat.size(); i++) {
                 //apply_rules(test, cat.get(i));
                    rule_test=cat.get(i);
                 System.out.println(apply_rules(test, rule_test) + " r=" + rule_test + " w=" + test);
             }
         }

        long finish = System.currentTimeMillis();
        System.out.println("time=" + (finish - start) + "ms");
        reader.close();
        pass.close();
       // bw.close();
    }

    public static String apply_rules(String in_str, String rules)
    {
        StringBuilder in=new StringBuilder(in_str);
        int rule_pos;
        int rule_len;
        int n, m, i;
        char x,y;
        String mem=new String();
        rule_len=rules.length();
        for (rule_pos = 0; rule_pos < rule_len; rule_pos++)
        {
            switch (rules.charAt(rule_pos)){
                case ' ':
                    break;
                case ':':                                              //	do nothing
                    break;
                case 'l':
                    if(in.length()>0) in=sb_to_lower_case(in);                           //	Lowercase all letters
                    break;
                case 'u':
                    if(in.length()>0) in=sb_to_upper_case(in);                           //	Uppercase all letters
                    break;
                case 'c':
                    if(in.length()>0) in=up_first_low_rest(in);                          //	Capitalize the first letter and lower the rest
                    break;
                case 'C':
                    if(in.length()>0) in=low_first_up_rest(in);                          //	Lowercase first found character, uppercase the rest
                    break;
                case 't':
                    if(in.length()>0) in=toggle_all(in);                                  //	Toggle the case of all characters in word.
                    break;
                case 'T':
                    if((++rule_pos)==rule_len) {in.delete(0, in.length()); break;}
                    n=get_num(rules, rule_pos);
                    if(n==-1) {in.delete(0, in.length()); break;}
                    in=toggle_c(in, n);                                 //Toggle the case of characters at position N
                    break;
                case 'r':
                    in=in.reverse();                                    //Reverse the entire word
                    break;
                case 'd':
                    in=in.append(in);                                   //	Duplicate entire word
                    break;
                case 'p':
                    if((++rule_pos)==rule_len) {in.delete(0, in.length()); break;}
                    n=get_num(rules, rule_pos);
                    if(n==-1) {in.delete(0, in.length()); break;}
                    in=duplicate_n_times(in, n);                        //Append duplicated word N times
                    break;
                case 'f':
                    in=duplicate_reversed(in);                          //Duplicate word reversed
                    break;
                case '{':
                    if(in.length()>0) in=rotate_left(in);
                    break;
                case '}':
                    if(in.length()>0) in=rotate_right(in);
                    break;
                case '$':
                    if((++rule_pos)==rule_len) {in.delete(0, in.length()); break;}
                    x=rules.charAt(rule_pos);
                    in=in.append(x);                                      //Append character X to end
                    break;
                case '^':
                    if((++rule_pos)==rule_len) {in.delete(0, in.length()); break;}
                    x=rules.charAt(rule_pos);
                    in=in.insert(0, x);                                    //Prepend character X to front
                    break;
                case '[':
                    if(in.length()>0) in=in.deleteCharAt(0);                                 //delete first character
                    break;
                case ']':
                    if(in.length()>0) in=in.deleteCharAt(in.length()-1);                     //delete last character
                    break;
                case 'D':
                    if((++rule_pos)==rule_len) {in.delete(0, in.length()); break;}
                    n=get_num(rules, rule_pos);
                    if(n==-1){in.delete(0, in.length()); break;}
                        if (n < in.length())
                            in = in.deleteCharAt(n);                                 //delete character at position n
                    break;
                case 'x':
                    if((++rule_pos)==rule_len) {in.delete(0, in.length()); break;}
                    n=get_num(rules, rule_pos);
                    if(n==-1) {in.delete(0, in.length()); break;}
                    if((++rule_pos)==rule_len) {in.delete(0, in.length()); break;}
                    m=get_num(rules, rule_pos);
                    if(m==-1) {in.delete(0, in.length()); break;}
                    in=extract_range(in,n,m);                             //Extracts M characters, starting at position N
                    break;
                case 'O':
                    if((++rule_pos)==rule_len) {in.delete(0, in.length()); break;}
                    n=get_num(rules, rule_pos);
                    if(n==-1) {in.delete(0, in.length()); break;}
                    if((++rule_pos)==rule_len) {in.delete(0, in.length()); break;}
                    m=get_num(rules, rule_pos);
                    if(m==-1) {in.delete(0, in.length()); break;}
                    if(n<in.length() && (n+m)<=in.length()) in=in.delete(n, n+m);                                 //Deletes M characters, starting at position N
                    break;
                case 'i':
                    if((rule_pos++)==rule_len) break;
                    n=get_num(rules, rule_pos);
                    if(n==-1) {in.delete(0, in.length()); break;}
                    if((++rule_pos)==rule_len) {in.delete(0, in.length()); break;}
                    x=rules.charAt(rule_pos);
                    if(n<=in.length()) in=in.insert(n, x);                                    //Inserts character X at position N
                    break;
                case 'o':
                    if((++rule_pos)==rule_len) {in.delete(0, in.length()); break;}
                    n=get_num(rules, rule_pos);
                    if(n==-1) {in.delete(0, in.length()); break;}
                    if((++rule_pos)==rule_len) {in.delete(0, in.length()); break;}
                    x=rules.charAt(rule_pos);
                    if(n<in.length()) in.setCharAt(n, x);                                     //Overwrites character at position N with X
                    break;
                case '\'':
                    if((++rule_pos)==rule_len) {in.delete(0, in.length()); break;}
                    n=get_num(rules, rule_pos);
                    if(n==-1) {in.delete(0, in.length()); break;}
                    if(n<in.length()) in=in.delete(n, in.length());                         //Truncate word at position N
                    break;
                case 's':
                    if((++rule_pos)==rule_len) {in.delete(0, in.length()); break;}
                    x=rules.charAt(rule_pos);
                    if((++rule_pos)==rule_len) {in.delete(0, in.length()); break;}
                    y=rules.charAt(rule_pos);
                    in=replace_char(in, x, y);                              //Replace all instances of X with Y
                    break;
                case 'S':
                    if((++rule_pos)==rule_len) {in.delete(0, in.length()); break;}
                    x=rules.charAt(rule_pos);
                    if((++rule_pos)==rule_len) {in.delete(0, in.length()); break;}
                    y=rules.charAt(rule_pos);
                    in=replace_char(in, x, y);                              //Replace all instances of X with Y
                    break;

                case '@':
                    if((++rule_pos)==rule_len) {in.delete(0, in.length()); break;}
                    x=rules.charAt(rule_pos);
                    in=purge(in, x);                                         //Purge all instances of X
                    break;
                case 'z':
                    if((++rule_pos)==rule_len) {in.delete(0, in.length()); break;}
                    n=get_num(rules, rule_pos);
                    if(n==-1) {in.delete(0, in.length()); break;}
                    if(in.length()>0)in=duplicate_first_n_times(in, n);                       //Duplicates first character N times
                    break;
                case 'Z':
                    if((++rule_pos)==rule_len) {in.delete(0, in.length()); break;}
                    n=get_num(rules, rule_pos);
                    if(n==-1) {in.delete(0, in.length()); break;}
                    if(in.length()>0) in=duplicate_last_n_times(in, n);                        //Duplicates last character N times
                    break;
                case 'q':
                    in=duplicate_every_ch(in);                               //Duplicate every character
                    break;
                case 'X':
                    if((++rule_pos)==rule_len) {in.delete(0, in.length()); break;}
                    n=get_num(rules, rule_pos);
                    if(n==-1) {in.delete(0, in.length()); break;}
                    if((++rule_pos)==rule_len) {in.delete(0, in.length()); break;}
                    m=get_num(rules, rule_pos);
                    if(m==-1) {in.delete(0, in.length()); break;}
                    if((++rule_pos)==rule_len) {in.delete(0, in.length()); break;}
                    i=get_num(rules, rule_pos);
                    if(i==-1) {in.delete(0, in.length()); break;}
                    if(i<=in.length()&& (n+m)<=mem.length()) in=append_substr_from_mem(in,mem, n, m, i);               // Insert substring of length M starting from position N of word saved to memory at position I
                    break;
                case '4':
                    in=in.append(mem);                                        // Append the word saved to memory to current word
                    break;
                case '6':
                    in=in.insert(0, mem);                                     // Prepend the word saved to memory to current word
                    break;
                case 'M':
                    mem=in.toString();                                        //Memorize current word
                    // System.out.println("in memory ="+ mem);
                    break;
                case 'k':
                    in=swap_front(in);                                        //Swaps first two characters
                    break;
                case 'K':
                    in=swap_back(in);                                         // Swaps last two characters
                    break;
                case '*':
                    if((++rule_pos)==rule_len) {in.delete(0, in.length()); break;}
                    n=get_num(rules, rule_pos);
                    if(n==-1) {in.delete(0, in.length()); break;}
                    if((++rule_pos)==rule_len) {in.delete(0, in.length()); break;}
                    m=get_num(rules, rule_pos);
                    if(m==-1) {in.delete(0, in.length()); break;}
                    if(n<in.length() && m<in.length())in=swap_n(in, n, m);    //Swaps character at position N with character at position M
                    break;
                case 'L':
                    if((++rule_pos)==rule_len) {in.delete(0, in.length()); break;}
                    n=get_num(rules, rule_pos);
                    if(n==-1) {in.delete(0, in.length()); break;}
                    if(n<in.length()) in.setCharAt(n, (char)(in.charAt(n)<<1));                 //Bitwise shift left character @ N
                    break;
                case 'R':
                    if((++rule_pos)==rule_len) {in.delete(0, in.length()); break;}
                    n=get_num(rules, rule_pos);
                    if(n==-1) {in.delete(0, in.length()); break;}
                    if(n<in.length())in.setCharAt(n, (char)(in.charAt(n)>>1));                 //Bitwise shift right character @ N
                    break;
                case '+':
                    if((++rule_pos)==rule_len) {in.delete(0, in.length()); break;}
                    n=get_num(rules, rule_pos);
                    if(n==-1) {in.delete(0, in.length()); break;}
                    if(n<in.length())in.setCharAt(n, (char)(in.charAt(n)+1));                  //Increment character @ N by 1 ascii value
                    break;
                case '-':
                    if((++rule_pos)==rule_len) {in.delete(0, in.length()); break;}
                    n=get_num(rules, rule_pos);
                    if(n==-1) {in.delete(0, in.length()); break;}
                    if(n<in.length()) in.setCharAt(n, (char)(in.charAt(n)-1));                  //Decrement character @ N by 1 ascii value
                    break;
                case '.':
                    if((++rule_pos)==rule_len) {in.delete(0, in.length()); break;}
                    n=get_num(rules, rule_pos);
                    if(n==-1) {in.delete(0, in.length()); break;}
                    if(n<in.length()-1) in.setCharAt(n, in.charAt(n+1));                 //Replaces character @ N with value at @ N plus 1
                    break;
                case ',':
                    if((++rule_pos)==rule_len) {in.delete(0, in.length()); break;}
                    n=get_num(rules, rule_pos);
                    if(n==-1) {in.delete(0, in.length()); break;}
                    if(n>0 && n<in.length()) in.setCharAt(n, in.charAt(n-1));                  //Replaces character @ N with value at @ N minus 1
                    break;
                case 'y':
                    if((++rule_pos)==rule_len) {in.delete(0, in.length()); break;}
                    n=get_num(rules, rule_pos);
                    if(n==-1) {in.delete(0, in.length()); break;}
                    in=duplicate_block_front(in,n);                           //Duplicates first N characters
                    break;
                case 'Y':
                    if((++rule_pos)==rule_len) {in.delete(0, in.length()); break;}
                    n=get_num(rules, rule_pos);
                    if(n==-1) {in.delete(0, in.length()); break;}
                    in=duplicate_block_back(in,n);                            //Duplicates last N characters
                    break;
                case 'E':
                    in=title(in);                                            //Lower case the whole line, then upper case the first letter and every letter after a space
                    break;
                case 'e':
                    if((++rule_pos)==rule_len) {in.delete(0, in.length()); break;}
                    x=rules.charAt(rule_pos);
                    if(in.length()!=0) in=title_w_separator(in, x);                             //Lower case the whole line, then upper case the first letter and every letter after a custom separator character
                    break;
                default:
                    in.delete(0, in.length()); break;
            }
        }
        return in.toString();
    }

    static int get_num(String r, int n){
        char c;
        c=r.charAt(n);
        if (is_num(c))
        {
            return c - '0';
        }
        else if (is_upper(c))
        {
            return c - 'A' + 10;
        }
        else return -1;
    }
    static boolean is_num(char c){
        return ((c >= '0') && (c <= '9'));
    }
    static boolean is_upper(char c){
        return ((c >= 'A') && (c <= 'Z'));
    }
    static boolean is_lower(char c){
        return ((c >= 'a') && (c <= 'z'));
    }
    static boolean is_letter(char c){
        return (is_upper(c) || is_lower(c));
    }
    static char to_lower_case(char c){
        if(is_upper(c)) c^=0x20;
        return c;
    }
    static char to_upper_case(char c){
        if(is_lower(c)) c^=0x20;
        return c;
    }
    static char toggle(char c){
        if(is_letter(c)) c^=0x20;
        return c;
    }


    static StringBuilder sb_to_lower_case(StringBuilder sb)
    {
        int len=sb.length();
        for(int i=0;i<sb.length();i++)
            sb.setCharAt(i, to_lower_case(sb.charAt(i)));
        return sb;
    }
    static StringBuilder sb_to_upper_case(StringBuilder sb)
    {
        int len=sb.length();
        for(int i=0;i<len;i++)
            sb.setCharAt(i, to_upper_case(sb.charAt(i)));
        return sb;
    }
    static StringBuilder up_first_low_rest(StringBuilder sb){
        sb.setCharAt(0, to_upper_case(sb.charAt(0)));
        int len=sb.length();
        for(int i=1;i< len;i++)
            sb.setCharAt(i, to_lower_case(sb.charAt(i)));
        return sb;
    }
    static StringBuilder low_first_up_rest(StringBuilder sb){
        sb.setCharAt(0, to_lower_case(sb.charAt(0)));
        int len=sb.length();
        for(int i=1;i<len; i++)
            sb.setCharAt(i, to_upper_case(sb.charAt(i)));
        return sb;
    }
    static StringBuilder toggle_all(StringBuilder sb){
        int len=sb.length();
        for(int i=0;i<len;i++)
            sb.setCharAt(i, toggle(sb.charAt(i)));
        return sb;
    }
    static StringBuilder toggle_c(StringBuilder sb, int n){
        if(n<sb.length()) sb.setCharAt(n, toggle(sb.charAt(n)));
        return sb;
    }
    static StringBuilder duplicate_n_times(StringBuilder sb, int n){
        String str=sb.toString();
        for(int i=0;i<n;i++) sb=sb.append(str);
        return sb;
    }
    static StringBuilder duplicate_reversed(StringBuilder sb){
        String str=sb.toString();
        return sb.reverse().insert(0, str);
    }
    static StringBuilder rotate_left(StringBuilder sb){
        sb=sb.append(sb.charAt(0));
        sb=sb.deleteCharAt(0);
        return sb;
    }
    static StringBuilder rotate_right(StringBuilder sb){
        sb=sb.insert(0, sb.charAt(sb.length()-1));
        sb=sb.deleteCharAt(sb.length()-1);
        return sb;
    }
    static StringBuilder extract_range(StringBuilder sb, int n, int m){
        if(n<sb.length() && (n+m)<=sb.length()){
            CharSequence cs=sb.subSequence(n, n+m);
            sb=new StringBuilder(cs);
        }
        return sb;
    }
    static StringBuilder replace_char(StringBuilder sb, char x, char y){
        int len=sb.length();
        for (int i=0;i<len; i++){
            if(sb.charAt(i)==x){
                sb.setCharAt(i,y);
            }
        }
        return sb;
    }
    static StringBuilder purge(StringBuilder sb, char x){
        for (int i = 0; i < sb.length(); i++) {
            if (sb.charAt(i) == x){
                sb.deleteCharAt(i);
                i--;
            }
        }
        return sb;
    }
    static StringBuilder duplicate_first_n_times(StringBuilder sb, int n){
        char c=sb.charAt(0);
        for (int i=0;i<n; i++)
            sb=sb.insert(0, c);
        return sb;
    }
    static StringBuilder duplicate_last_n_times(StringBuilder sb, int n){
        char c=sb.charAt(sb.length()-1);
        for (int i=0;i<n; i++)
            sb=sb.append(c);
        return sb;
    }
    static StringBuilder duplicate_every_ch(StringBuilder sb){
        for (int i=0;i<sb.length(); i=i+2)
            sb=sb.insert(i, sb.charAt(i));
        return sb;
    }
    static StringBuilder append_substr_from_mem(StringBuilder sb, String mem, int n, int m, int i){
        mem=mem.substring(n, n+m);
        sb=sb.insert(i, mem);
        return sb;
    }
    static StringBuilder swap_front(StringBuilder sb){
        if (sb.length() > 1) {
            char t=sb.charAt(0);
            sb.setCharAt(0, sb.charAt(1));
            sb.setCharAt(1, t);
        }
        return sb;
    }
    static StringBuilder swap_back(StringBuilder sb){

        if (sb.length() > 1) {
            char t=sb.charAt(sb.length()-1);
            sb.setCharAt(sb.length() - 1, sb.charAt(sb.length() - 2));
            sb.setCharAt(sb.length() - 2, t);
        }
        return sb;
    }
    static StringBuilder swap_n(StringBuilder sb, int n, int m){
        char t=sb.charAt(n);
        sb.setCharAt(n, sb.charAt(m));
        sb.setCharAt(m, t);
        return sb;
    }
    static StringBuilder duplicate_block_front(StringBuilder sb, int n){
        if(n<=sb.length()) {
            String str = sb.substring(0, n);
            sb = sb.insert(0, str);
        }
        return sb;
    }
    static StringBuilder duplicate_block_back(StringBuilder sb, int n){
        int len=sb.length();
        if(n<=len) {
            String str = sb.substring(len - n, len);
            sb = sb.insert(len, str);
        }
        return sb;
    }
    static StringBuilder title(StringBuilder sb){
        sb_to_lower_case(sb);
        sb.setCharAt(0, to_upper_case(sb.charAt(0)));
        int len=sb.length();
        for(int i=0;i<(len-1);i++)
        {
            if(sb.charAt(i)==' ')  sb.setCharAt(i+1, to_upper_case(sb.charAt(i+1)));
        }
        return sb;
    }
    static StringBuilder title_w_separator(StringBuilder sb, char x){
        sb_to_lower_case(sb);
        sb.setCharAt(0, to_upper_case(sb.charAt(0)));
        int len=sb.length();
        for(int i=0;i<(len-1);i++)
        {
            if(sb.charAt(i)==x)  sb.setCharAt(i+1, to_upper_case(sb.charAt(i+1)));
        }
        return sb;
    }
}


