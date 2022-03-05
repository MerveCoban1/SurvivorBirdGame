package com.example.survivorbird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;

import java.util.Random;
/*
 * kodlarımızı burada yazcağız.
 * manifestte screenorientation landscape olacak yani yan kullanacağız.
 *opengameart.org sitesine gir bu sitede ücretsiz bir sürü oyun grafiği var.
 *asset'e gel androis studioda show in explorer de içine resimleri koy
 *
 *
 */

public class SurvivorBird extends ApplicationAdapter{
    //oyunlarda sprite denilen bazı nesnelerle çalışıyoruz. içine koyduğumuz kuşlar labeller vs vs bunlara sprite diyoruz.
    //backler spriteları çizmemiz için yardımcı olan metotlar objeler.
    SpriteBatch batch;
    //oyunlarda resmi bununla ifade ediyoruz.
    Texture background;
    Texture bird;
    Texture bee1;
    Texture star;
    Texture bee3;
    //kuşun yerini değiştireceğimiz için
    float birdX = 0, birdY = 0;
    //oyunun başladığını tutacağız
    int gameState = 0;
    //kuşu düşürmek için
    float velocity = 0;
    float gravity = 1f;
    //arılar sağdan sola doğru gidecekler
    //float enemyX=0;

    //bir sürü arı olucak 12 tane falan onları döngüye alıcaz
    int numberOfEnemies = 4;
    float[] enemyX = new float[numberOfEnemies];
    float distance = 0;//arılar arası uzaklık
    float enemyVelocity = 10;

    //şimdi arılarımızın y eksenini random yapacağız.
    float[] enemyOffset = new float[numberOfEnemies];
    float[] enemyOffset2 = new float[numberOfEnemies];
    float[] enemyOffset3 = new float[numberOfEnemies];
    Random random;

    //şimdi çarpışmaları yapacağız.
    Circle birdCircle;
    Circle[] enemyCircles;
    Circle[] starCircles;
    Circle[] enemyCircles3;
    ShapeRenderer shapeRenderer;//kullandığım circle'ı çizdiricem

    int score = 0;
    int enYuksekScore=0;
    int scoredEnemy = 0;
    BitmapFont font;//skoru yazdırmak için
    BitmapFont font2;

    BitmapFont font3;
    BitmapFont font4;
    BitmapFont font5;

    int puan=0;
    int enYuksekPuan=0;

    @Override//oyun açılınca olacak şeyleri yazıyoruz.
    public void create() {

        batch = new SpriteBatch();
        background = new Texture("background.png");
        bird = new Texture("bird.png");
        bee1 = new Texture("bee.png");
        star = new Texture("star.png");
        bee3 = new Texture("bee.png");

        random = new Random();

        distance = Gdx.graphics.getWidth()/2;//2 arı arasında ekranın yarısı kadar bir fark olsun.
        birdX = Gdx.graphics.getWidth() / 5;
        birdY = Gdx.graphics.getHeight() / 2;

        birdCircle = new Circle();
        enemyCircles = new Circle[numberOfEnemies];
        starCircles = new Circle[numberOfEnemies];
        enemyCircles3 = new Circle[numberOfEnemies];
        shapeRenderer = new ShapeRenderer();

        font = new BitmapFont();
        font.setColor(Color.valueOf("#09973D"));
        font.getData().setScale(4);
        font2 = new BitmapFont();
        font2.setColor(Color.valueOf("#09973D"));
        font2.getData().setScale(6);
        font3 = new BitmapFont();
        font3.setColor(Color.valueOf("#4e2b15"));
        font3.getData().setScale(3);
        font4 = new BitmapFont();
        font4.setColor(Color.valueOf("#4e2b15"));
        font4.getData().setScale(3);
        font5 = new BitmapFont();
        font5.setColor(Color.valueOf("#4e2b15"));
        font5.getData().setScale(3);

        for (int i = 0; i < numberOfEnemies; i++) {
            enemyOffset[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 500);
            enemyOffset2[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 500);
            enemyOffset3[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 500);

            enemyX[i] = Gdx.graphics.getWidth() - bee1.getWidth() / 2 + i * distance;

            enemyCircles[i] = new Circle();
            starCircles[i] = new Circle();
            enemyCircles3[i] = new Circle();
        }

    }

    @Override//oyun devam ettiği sürece devamlı çağırılan bir metot.
    //yani sürekli olmasını istediğimiz şeyleri render altında yazacağız.
    public void render() {
        batch.begin();
        //ekranın sıfır sıfır noktasından başlat Yani sol en alt dipten
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        if (gameState == 1) {
            if (enemyX[scoredEnemy] < Gdx.graphics.getWidth() / 5) {
                score++;
                if (scoredEnemy < numberOfEnemies - 1) {
                    scoredEnemy++;
                } else {
                    scoredEnemy = 0;
                }
            }

            if (Gdx.input.justTouched()) {
                velocity -= 28;
            }
            for (int i = 0; i < numberOfEnemies; i++) {
                if (enemyX[i] < Gdx.graphics.getWidth() / 15) {
                    //başa almaya çalıştık 4lü döngüyü
                    enemyX[i] = enemyX[i] + numberOfEnemies * distance;
                    enemyOffset[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
                    enemyOffset2[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
                    enemyOffset3[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
                } else {
                    enemyX[i] = enemyX[i] - enemyVelocity;
                }
                batch.draw(bee1, enemyX[i], Gdx.graphics.getHeight() / 2 + enemyOffset[i], Gdx.graphics.getWidth() / 15, Gdx.graphics.getHeight() / 10);
                batch.draw(star, enemyX[i], Gdx.graphics.getHeight() / 2 + enemyOffset2[i], Gdx.graphics.getWidth() / 15, Gdx.graphics.getHeight() / 10);
                batch.draw(bee3, enemyX[i], Gdx.graphics.getHeight() / 2 + enemyOffset3[i], Gdx.graphics.getWidth() / 15, Gdx.graphics.getHeight() / 10);

                enemyCircles[i] = new Circle(enemyX[i] + Gdx.graphics.getWidth() / 30, Gdx.graphics.getHeight() / 2 + enemyOffset[i] + Gdx.graphics.getHeight() / 20, Gdx.graphics.getWidth() / 30);
                starCircles[i] = new Circle(enemyX[i] + Gdx.graphics.getWidth() / 30, Gdx.graphics.getHeight() / 2 + enemyOffset2[i] + Gdx.graphics.getHeight() / 20, Gdx.graphics.getWidth() / 30);
                enemyCircles3[i] = new Circle(enemyX[i] + Gdx.graphics.getWidth() / 30, Gdx.graphics.getHeight() / 2 + enemyOffset3[i] + Gdx.graphics.getHeight() / 20, Gdx.graphics.getWidth() / 30);
            }
            if (birdY > 0&& birdY<Gdx.graphics.getHeight()) {
                //kuşumuza bir yer çekimi yaratmak istiyoruz.
                velocity += gravity;
                birdY -= velocity;
            }else{
                gameState = 2;
            }


        } else if (gameState == 0) {
            if (Gdx.input.justTouched()) {
                gameState = 1;
            }
        } else if (gameState == 2) {
            //oyun bitecek
            font2.draw(batch, "Game Over! Tap To Play Again!", 350, Gdx.graphics.getHeight() / 2);
            if (Gdx.input.justTouched()) {
                gameState = 1;
            }
            //oyunu yeniden başlatabiliriz.
            birdY = Gdx.graphics.getHeight() / 3;
            for (int i = 0; i < numberOfEnemies; i++) {
                enemyOffset[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
                enemyOffset2[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
                enemyOffset3[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);

                enemyX[i] = Gdx.graphics.getWidth() - bee1.getWidth() / 2 + i * distance;

                enemyCircles[i] = new Circle();
                starCircles[i] = new Circle();
                enemyCircles3[i] = new Circle();
            }
            velocity = 0;
            scoredEnemy = 0;
            if (score>enYuksekScore){
                enYuksekScore=score;
            }
            if (puan>enYuksekPuan){
                enYuksekPuan=puan;
            }
            score = 0;
            puan=0;
        }

        //ilk x ve y konumu sonrakiler boyutu
        batch.draw(bird, birdX, birdY, Gdx.graphics.getWidth() / 15, Gdx.graphics.getHeight() / 10);
        font.draw(batch, String.valueOf(score), 100, 225);
        font3.draw(batch, "Best Score: "+enYuksekScore, 1400, 1000);
        font4.draw(batch, "Point: "+puan, 60, 150);
        font5.draw(batch, "Best Point: "+enYuksekPuan, 1400, 925);
        batch.end();

        birdCircle.set(birdX + Gdx.graphics.getWidth() / 30, birdY + Gdx.graphics.getHeight() / 20, Gdx.graphics.getWidth() / 30);
        // shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        // shapeRenderer.setColor(Color.BLACK);
        // shapeRenderer.circle(birdCircle.x,birdCircle.y,birdCircle.radius);
        for (int i = 0; i < numberOfEnemies; i++) {
            //shapeRenderer.circle(enemyX[i]+Gdx.graphics.getWidth() / 30,Gdx.graphics.getHeight() / 2 + enemyOffset[i]+Gdx.graphics.getHeight() / 20,Gdx.graphics.getWidth() / 30);
            // shapeRenderer.circle(enemyX[i]+Gdx.graphics.getWidth() / 30,Gdx.graphics.getHeight() / 2 + enemyOffset2[i]+Gdx.graphics.getHeight() / 20,Gdx.graphics.getWidth() / 30);
            //  shapeRenderer.circle(enemyX[i]+Gdx.graphics.getWidth() / 30,Gdx.graphics.getHeight() / 2 + enemyOffset3[i]+Gdx.graphics.getHeight() / 20,Gdx.graphics.getWidth() / 30);
            //şimdi çarpışmaları yapalım
            if (Intersector.overlaps(birdCircle, enemyCircles[i])  || Intersector.overlaps(birdCircle, enemyCircles3[i])) {
                gameState = 2;
            }
            if (Intersector.overlaps(birdCircle, starCircles[i])){
                puan++;
            }
        }
        // shapeRenderer.end(); shaperender'ları kapattık. Artık doğru yerde olduklarını anladık çünkü

    }

    @Override
    public void dispose() {

    }
}
