# JavaTriviaProject
Collaboration project in Java utilizing a trivia web API to implement a GUI for users to play a trivia game.


## OpenTriviaDB

### URL Format for GET Request/Query

```https://opentdb.com/api.php?amount=%d&category=%d&difficulty=%s&type=%s```

#### Notes on Format

%d = decimal/numerical

%s = string

##### Amount:
* Maxes out at 50. 1 <= X <= 50

##### API Categories:
9. <option value="9">General Knowledge</option>
10. <option value="10">Entertainment: Books</option>
11. <option value="11">Entertainment: Film</option>
12. <option value="12">Entertainment: Music</option>
13. <option value="13">Entertainment: Musicals &amp; Theatres</option>
14. <option value="14">Entertainment: Television</option>
15. <option value="15">Entertainment: Video Games</option>
16. <option value="16">Entertainment: Board Games</option>
17. <option value="17">Science &amp; Nature</option>
18. <option value="18">Science: Computers</option>
19. <option value="19">Science: Mathematics</option>
20. <option value="20">Mythology</option>
21. <option value="21">Sports</option>
22. <option value="22">Geography</option>
23. <option value="23">History</option>
24. <option value="24">Politics</option>
25. <option value="25">Art</option>
26. <option value="26">Celebrities</option>
27. <option value="27">Animals</option>
28. <option value="28">Vehicles</option>
29. <option value="29">Entertainment: Comics</option>
30. <option value="30">Science: Gadgets</option>
31. <option value="31">Entertainment: Japanese Anime &amp; Manga</option>
32. <option value="32">Entertainment: Cartoon &amp; Animations</option>	

##### Logical Categorization Suggestion

- General Knowledge [9]
- Entertainment [10..16, 21, 26, 29, 31..32]
  * Books, Film, Music, Theater, TV, Video Games, Board Games, Sports, Celebrities, Comics, Anime, Cartoons
- Science [17..19, 27, 30]
  * Science & Nature, Computers, Mathematics, Animals (taxonomy?), Gadgets
- History and the World [20, 22..25]
  * Mythology, Geography, History, Politics
- Random [Any]

//Where do we put vehicles?