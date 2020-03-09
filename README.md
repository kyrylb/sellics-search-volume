###Algorithm

The logic of this algorithm is: we take into account only search by part of the keyword, not the exact search.
The less part of the original input is found - the more searched is the input.

For instance, if I search "iphone charger" and get "iphone charger" - it is not surprising, but if I search the string 
"iphone" and still get "iphone charger" - it means this string is searched very often.

So, basically, we remove one character from the input and check, do we have original input in the responce?

The final score is sum of every partial search. The score of every partial search is based upon the assumptions:
1) the smaller the input is, the more weight it has;
2) the place in the list returned - if it's higher, it has better search volume.

The weight is counted in such way: 100 is the total weight. We found, how much weight we have per char position 
(we find number of positions like sum of natural numbers from 1 to length-1 )
`        int sumOfPartialCharCounts = maxPartialCharCount * (maxPartialCharCount + 1) / 2;
`        
Then we divide 100 by this number and have weightCoefficient per 1 position. 
`double weightCoefficient = 100.0 / sumOfPartialCharCounts;
`
And multiply position and this number to have array of weights like:
weight = [40.0, 30.0, 20.0, 10.0]

weight[0] is weight for the search with single char, 
weight[1] is weight for 2-chars etc.

Then we run REST requests to get autocomplete list for each partial search. The upper the original keyword in the list -
the bigger is the score. We divide appropriate weight with position in the list (with coefficient).
So, if each partial search contains original keyword at the top position, we have:
`40.0 / 1 + 30.0 / 1 + 20.0 / 1 + 10.0 / 1 => 100.0`

