![mingdx-logo](https://user-images.githubusercontent.com/50753501/97774186-13e76000-1b56-11eb-804d-f89aba2681b2.png)

![Release](https://jitpack.io/v/iSoterikTechnologies/mingdx.svg)

[minGDX](https://isoteriksoftware.gitbook.io/mingdx/) **is a small open source Java game development library based on [libGDX](https://libgdx.com/).
It is neither an alternative nor a replacement for libGDX, it simply provides another (better) way of coding a libGDX game. minGDX is not an extension of libGDX either!**

We all love libGDX, it is cross-platform, and most of the time, it gets the job done. However, just like any other framework, liGDX can't handle every needs of a game.
This is because games are usually too specific, so it is hard to anticipate the developer's needs. libGDX already handles most of the low-level details that we (libGDX users)
don't have to deal with ourselves, however, there are still some boiler plates that we keep repeating for every project, and it gets worse.
minGDX takes care of common boiler plates and presents a clean component-based API for developing your libGDX game.

Just in case you're wondering what the **min** in minGDX means, it stands for _minimal_, as in **minimal libGDX**. We're terrible at naming things, yes we know :)


## Getting minGDX / Documentation
minGDX is dependent on two of the main libGDX extensions: **box2d** and **gdx-ai**. When creating your libGDX project, it is advisable to add those extensions at that point.

minGDX is available in JitPack:
- Add JitPack in your root build.gradle at the end of repositories:
```shell
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
- To pull minGDX, open the project-level build.gradle file, add `api 'com.github.iSoterikTechnologies:mingdx:v2.0.0'` to the dependencies closure of the core project.
Sync the project and wait for gradle to do its magic!

**Note:** We used _api_ instead of _implementation_ because we want the other sub-projects (modules) to be able to access minGDX. This is possible because the sub-projects
depend on the core project. If we don't do this, then we must add the dependecy to every other module!

Though minGDX is a relatively small library, it is very well documented! We offer a regularly updated tutorial blog dedicated to minGDX and a clean documentation website:
- [Visit the tutorial blog](https://gdx-gaming.blogspot.com)
- [MinGdx documentation](https://isoteriksoftware.gitbook.io/mingdx/)
- [Javadocs](https://isoteriksoftware.github.io/mingdx/)


## Usage
It is very easy to get started with minGDX. Integrate minGDX in 4 easy steps:
- Make your game class extend `com.isoterik.mgdx.MinGdxGame`:
```java
import com.isoterik.mgdx.MinGdxGame;

public class MyGameClass extends MinGdxGame {

}
```
- Override the `initGame()` method and return a `com.isoterik.mgdx.Scene` instance (this will be your splash scene!):
```java
import com.isoterik.mgdx.MinGdxGame;
import com.isoterik.mgdx.Scene;

public class MyGameClass extends MinGdxGame {
    @Override
    protected Scene initGame() {
        return new Scene();
    }
}
```
- Optionally set a scene transition animation:
```java
import com.isoterik.mgdx.MinGdxGame;
import com.isoterik.mgdx.Scene;
import com.isoterik.mgdx.m2d.scenes.transition.SceneTransitions;

public class MyGameClass extends MinGdxGame {
    @Override
    protected Scene initGame() {
        splashTransition = SceneTransitions.fade(1f); // Let's fade in!
        return new Scene();
    }
}
```
- Run your game!

If you see a red background displayed then congratulations, you've just ran your first minGDX game!


## Support
We've only just scratched the surface! Head on to our [tutorial blog](https://gdx-gaming.blogspot.com) to learn more.
Check out the [documentations](https://isoteriksoftware.gitbook.io/mingdx/) and, also the [minGDX javadocs](https://isoteriksoftware.github.io/mingdx/).


## License
Just like libGDX, minGDX is licensed under [Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0.html) meaning you can use it free of charge, without strings attached in commercial and non-commercial projects.
We, however, love to get (non-mandatory) credit in case you release a game or app using minGDX!
