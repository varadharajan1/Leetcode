/*
Implement regular expression matching with support for '.' and '*'.

'.' Matches any single character.
'*' Matches zero or more of the preceding element.

The matching should cover the entire input string (not partial).

The function prototype should be:
bool isMatch(const char *s, const char *p)

Some examples:
isMatch("aa","a") → false
isMatch("aa","aa") → true
isMatch("aaa","aa") → false
isMatch("aa", "a*") → true
isMatch("aa", ".*") → true
isMatch("ab", ".*") → true
isMatch("aab", "c*a*b") → true
*/
// Fork from gaohannk.
/* 假设现在走到s的i位置，p的j位置，情况分为下列两种： 
 * (1)p[j+1]不是'*'。
 * 情况比较简单，只要判断当前s的i和p的j上的字符是否一样（p在j上的字符是'.',也是相同），如果不同，返回false，否则，递归下一层i+1，j+1; 
 * (2)p[j+1]是'*'。
 * 那么此时看从s[i]开始的子串，假设s[i],s[i+1],...s[i+k]都等于p[j]那么意味着这些都有可能是合适的匹配，
 * 那么递归对于剩下的(i,j+2),(i+1,j+2),...,(i+k,j+2)都要尝试（j+2是因为跳过当前和下一个'*'字符）。 
 */
 // 此题主要在于判断j + 1而不是j. 由于递归，前面要加限制条件。由于是指针，则限制条件为越界和长度的限定。
 // 可参考http://blog.welkinlan.com/2015/04/26/regular-expression-matching-wildcard-matching-leetcode-java-dp/的DP图。和http://www.cnblogs.com/jdflyfly/p/3810681.html的三种解法。
 // 此题的题目是，"*"代表“*”之前的那个字符，0次或数次。
 // http://www.jianshu.com/p/c09c4a3fc14a 讲述了DP. 然而不太理解为什么初始化时候空字符串能匹配X*。 
 
/***
* 为什么isMatch("aab", "c*a*b") → true? 	c*代表0个c. 在计算理论课程中有提到。
http://articles.leetcode.com/regular-expression-matching
*/
// 补充。“aa, a*a”为true
/*
第二种方法用DP做。
if p.charAt(j) == s.charAt(i) || p.charAt(j) == '.'
	dp[i][j] = dp[i-1][j-1]
If p.charAt(j) == '*':
	if p.charAt(j-1) != s.charAt(i)
		dp[i][j] = dp[i][j-2]  //in this case, a* only counts as empty
 	if p.charAt(i-1) == s.charAt(i) || p.charAt(i-1) == '.':
                dp[i][j] = dp[i-1][j]    //in this case, a* counts as multiple a 
        	dp[i][j] = dp[i][j-2]   // in this case, a* counts as empty
*/

public class Solution {
    public boolean isMatch(String s, String p) {
		return helper(s, p, 0, 0);
	}
	private boolean helper(String s, String p, int i, int j) {
		if (j == p.length())
			return i == s.length();
		if (j == p.length() - 1 || p.charAt(j + 1) != '*') {                                // p.charAt(j + 1) != '*'
			if (i == s.length() || s.charAt(i) != p.charAt(j) && p.charAt(j) != '.')
				return false;
			else
				return helper(s, p, i + 1, j + 1);
		}
		else {                                                                             // p.charAt(j + 1) == '*'
			while (i < s.length() && (p.charAt(j) == '.' || s.charAt(i) == p.charAt(j))) {
				if (helper(s, p, i, j + 2))		 	// 例如"aa"与“a*a”. 要从i 和 j + 2递归起。
					return true;                           // 有true则为true.
				i++;                                           // 例如“aa”与“a*”, i加到2
			}
			return helper(s, p, i, j + 2);              // 如果一直都相同字母但是FALSE，从不同字母和j+2继续递归
		}
	}
	
	public boolean isMatch2(String s, String p) {
		int m = s.length();
		int n = p.length();
		boolean[][] dp = new boolean[m + 1][n + 1];
		dp[0][0] = true;
		for (int i = 1; i <= m; i++)
			dp[i][0] = false;
		for (int j = 1; j <= n; j++) {
			if (j > 1 && p.charAt(j - 1) == '*')
				dp[0][j] = dp[0][j - 2];
		}
		for(int i = 1; i <= m; i++){
			for (int j = 1; j <= n; j++) {
				if (s.charAt(i - 1) == p.charAt(j - 1) || p.charAt(j - 1) == '.')
					dp[i][j] = dp[i - 1][j - 1];
				else if (p.charAt(j - 1) == '*') {
					if (s.charAt(i - 1) == p.charAt(j - 2) || p.charAt(j - 2) == '.')
						dp[i][j] = dp[i - 1][j] || dp[i][j - 2];
					else
						dp[i][j] = dp[i][j - 2];
				}
			}
		} 
		return dp[m][n];
	}
}
