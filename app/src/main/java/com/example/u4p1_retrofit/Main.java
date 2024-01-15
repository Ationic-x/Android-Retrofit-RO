package com.example.u4p1_retrofit;

import com.squareup.moshi.Json;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;
import retrofit2.http.*;

import java.util.List;

public class Main {
    public static class Ai{
        @Json(name = "mapValue")
        public MapValue mapValue;
    }

    public static class Mob {
        @Json(name = "name")
        public String name;
        @Json(name = "fields")
        public Fields fields;

        public List<Drop> drops;
    }

    public static class ElementStats{
        @Json(name = "mapValue")
        public MapValue mapValue;
    }

    public static class Fields{
        @Json(name = "Luk")
        public IntegerValue luk;
        @Json(name = "Dex")
        public IntegerValue dex;
        @Json(name = "BaseExp")
        public IntegerValue baseExp;
        @Json(name = "Race")
        public StringValue race;
        @Json(name = "Defense")
        public IntegerValue defense;
        @Json(name = "ChaseRange")
        public IntegerValue chaseRange;
        @Json(name = "Element")
        public StringValue element;
        @Json(name = "Vit")
        public IntegerValue vit;
        @Json(name = "WalkSpeed")
        public IntegerValue walkSpeed;
        @Json(name = "JobExp")
        public IntegerValue jobExp;
        @Json(name = "Int")
        public IntegerValue _int;
        @Json(name = "Ai")
        public Ai ai;
        @Json(name = "Level")
        public IntegerValue level;
        @Json(name = "Attack2")
        public IntegerValue attack2;
        @Json(name = "MagicDefense")
        public IntegerValue magicDefense;
        public StringValue img;
        @Json(name = "ElementLevel")
        public IntegerValue elementLevel;
        @Json(name = "SkillRange")
        public IntegerValue skillRange;
        @Json(name = "Hp")
        public IntegerValue hp;
        @Json(name = "ElementStats")
        public ElementStats elementStats;
        @Json(name = "AttackMotion")
        public IntegerValue attackMotion;
        @Json(name = "Attack")
        public IntegerValue attack;
        @Json(name = "AttackRange")
        public IntegerValue attackRange;
        @Json(name = "AttackDelay")
        public IntegerValue attackDelay;
        @Json(name = "Modes")
        public Modes modes;
        @Json(name = "Name")
        public StringValue name;
        @Json(name = "AegisName")
        public StringValue aegisName;
        @Json(name = "Agi")
        public IntegerValue agi;
        @Json(name = "Str")
        public IntegerValue str;
        @Json(name = "Size")
        public StringValue size;
        @Json(name = "Fire")
        public IntegerValue fire;
        @Json(name = "Water")
        public IntegerValue water;
        @Json(name = "Holy")
        public IntegerValue holy;
        @Json(name = "Neutral")
        public IntegerValue neutral;
        @Json(name = "Wind")
        public IntegerValue wind;
        @Json(name = "Undead")
        public IntegerValue undead;
        @Json(name = "Earth")
        public IntegerValue earth;
        @Json(name = "Dark")
        public IntegerValue dark;
        @Json(name = "Poison")
        public IntegerValue poison;
        @Json(name = "Ghost")
        public IntegerValue ghost;
        @Json(name = "CanMove")
        public BooleanValue canMove;
        @Json(name = "Looter")
        public BooleanValue looter;
        @Json(name = "Aggressive")
        public BooleanValue aggressive;
        @Json(name = "Assist")
        public BooleanValue assist;
        @Json(name = "CastSensorIdle")
        public BooleanValue castSensorIdle;
        @Json(name = "NoRandomWalk")
        public BooleanValue noRandomWalk;
        @Json(name = "NoCast")
        public BooleanValue noCast;
        @Json(name = "CanAttack")
        public BooleanValue canAttack;
        @Json(name = "CastSensorChase")
        public BooleanValue castSensorChase;
        @Json(name = "ChangeChase")
        public BooleanValue changeChase;
        @Json(name = "Angry")
        public BooleanValue angry;
        @Json(name = "ChangeTargetMelee")
        public BooleanValue changeTargetMelee;
        @Json(name = "ChangeTargetChase")
        public BooleanValue changeTargetChase;
        @Json(name = "TargetWeak")
        public BooleanValue targetWeak;
        @Json(name = "RandomTarget")
        public BooleanValue randomTarget;
        @Json(name = "IgnoreMelee")
        public BooleanValue ignoreMelee;
        @Json(name = "IgnoreMagic")
        public BooleanValue ignoreMagic;
        @Json(name = "IgnoreRanged")
        public BooleanValue ignoreRanged;
        @Json(name = "Mvp")
        public BooleanValue mvp;
        @Json(name = "IgnoreMisc")
        public BooleanValue ignoreMisc;
        @Json(name = "KnockBackImmune")
        public BooleanValue knockBackImmune;
        @Json(name = "TeleportBlock")
        public BooleanValue teleportBlock;
        @Json(name = "FixedItemDrop")
        public BooleanValue fixedItemDrop;
        @Json(name = "Detector")
        public BooleanValue detector;
        @Json(name = "StatusImmune")
        public BooleanValue statusImmune;
        @Json(name = "SkillImmune")
        public BooleanValue skillImmune;
    }
    public static class MapValue{
        @Json(name = "fields")
        public Fields fields;
    }

    public static class Modes{
        @Json(name = "mapValue")
        public MapValue mapValue;
    }

    public static class AllMobs {
        @Json(name = "documents")
        public List<Mob> mobs;
        @Json(name = "nextPageToken")
        public String nextPageToken;
    }

    // Drops Case

    public static class AllDrops {
        @Json(name = "documents")
        public List<Drop> drops;
    }

    public static class Drop {
        @Json(name = "fields")
        public DropFields fields;
    }

    public static class DropFields {
        @Json(name = "Rate")
        public IntegerValue rate;

        @Json(name = "Item")
        public StringValue item;
    }

    // Generics
    public static class IntegerValue{
        @Json(name = "integerValue")
        public String integerValue;
    }

    public static class StringValue{
        @Json(name = "stringValue")
        public String stringValue;
    }

    public static class BooleanValue{
        @Json(name = "booleanValue")
        public Boolean booleanValue;
    }

    public static Api api = new Retrofit.Builder()
            .baseUrl("https://firestore.googleapis.com/v1/projects/u4p1retrofit-a64c4/databases/(default)/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(Api.class);

    public interface Api {
        @GET("documents/mobs/")
        Call<AllMobs> getMobs(@Query("pageToken") String pageToken);
    }
}
