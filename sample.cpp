/* header files */
#include <bits/stdc++.h>
using namespace std;

/* typedef constants */
typedef long int li;
typedef unsigned long int uli;
typedef long long int lli;
typedef unsigned long long int ulli;
typedef vector<int> vi;
typedef vector<string> vs;

int main () {
   ios_base::sync_with_stdio(false);
   cin.tie(NULL);
   #ifndef ONLINE_JUDGE
   freopen("input.txt", "r", stdin);
   freopen("error.txt", "w", stderr);
   freopen("output.txt", "w", stdout);
   #endif
   /* Code Below */
   int s, n;
   vector<pair<int, int>> v;
   cin >> s >> n;
   while (n--) {
      int a, b;
      cin >> a >> b;
      v.push_back(make_pair(a, b));
   }
   sort(v.begin(), v.end());
   for (int i = 0; i < v.size(); i++) {
      if (s > v[i].first) {
         s += v[i].second;
      }
      else {
         cout << "NO" << endl;
         return 0;
      }
   }
   cout << "YES" << endl;
   return 0;
}
