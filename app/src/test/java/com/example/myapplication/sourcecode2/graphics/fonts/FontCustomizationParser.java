/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.myapplication.sourcecode2.graphics.fonts;

import static android.text.FontConfig.Alias;
import static android.text.FontConfig.NamedFamilyList;

import android.annotation.NonNull;
import android.annotation.Nullable;
import android.graphics.FontListParser;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Parser for font customization
 *
 * @hide
 */
public class FontCustomizationParser {
    private static final String TAG = "FontCustomizationParser";

    /**
     * Represents a customization XML
     */
    public static class Result {
        private final Map<String, NamedFamilyList> mAdditionalNamedFamilies;

        private final List<Alias> mAdditionalAliases;

        public Result() {
            mAdditionalNamedFamilies = Collections.emptyMap();
            mAdditionalAliases = Collections.emptyList();
        }

        public Result(Map<String, NamedFamilyList> additionalNamedFamilies,
                List<Alias> additionalAliases) {
            mAdditionalNamedFamilies = additionalNamedFamilies;
            mAdditionalAliases = additionalAliases;
        }

        public Map<String, NamedFamilyList> getAdditionalNamedFamilies() {
            return mAdditionalNamedFamilies;
        }

        public List<Alias> getAdditionalAliases() {
            return mAdditionalAliases;
        }
    }

    /**
     * Parses the customization XML
     *
     * Caller must close the input stream
     */
    public static Result parse(
            @NonNull InputStream in,
            @NonNull String fontDir,
            @Nullable Map<String, File> updatableFontMap
    ) throws XmlPullParserException, IOException {
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(in, null);
        parser.nextTag();
        return readFamilies(parser, fontDir, updatableFontMap);
    }

    private static Result validateAndTransformToResult(
            List<NamedFamilyList> families, List<Alias> aliases) {
        HashMap<String, NamedFamilyList> namedFamily = new HashMap<>();
        for (int i = 0; i < families.size(); ++i) {
            final NamedFamilyList family = families.get(i);
            final String name = family.getName();
            if (name != null) {
                if (namedFamily.put(name, family) != null) {
                    throw new IllegalArgumentException(
                            "new-named-family requires unique name attribute");
                }
            } else {
                throw new IllegalArgumentException(
                        "new-named-family requires name attribute or new-default-fallback-family"
                                + "requires fallackTarget attribute");
            }
        }
        return new Result(namedFamily, aliases);
    }

    private static Result readFamilies(
            @NonNull XmlPullParser parser,
            @NonNull String fontDir,
            @Nullable Map<String, File> updatableFontMap
    ) throws XmlPullParserException, IOException {
        List<NamedFamilyList> families = new ArrayList<>();
        List<Alias> aliases = new ArrayList<>();
        parser.require(XmlPullParser.START_TAG, null, "fonts-modification");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) continue;
            String tag = parser.getName();
            if (tag.equals("family")) {
                readFamily(parser, fontDir, families, updatableFontMap);
            } else if (tag.equals("family-list")) {
                readFamilyList(parser, fontDir, families, updatableFontMap);
            } else if (tag.equals("alias")) {
                aliases.add(FontListParser.readAlias(parser));
            } else {
                FontListParser.skip(parser);
            }
        }
        return validateAndTransformToResult(families, aliases);
    }

    private static void readFamily(
            @NonNull XmlPullParser parser,
            @NonNull String fontDir,
            @NonNull List<NamedFamilyList> out,
            @Nullable Map<String, File> updatableFontMap)
            throws XmlPullParserException, IOException {
        final String customizationType = parser.getAttributeValue(null, "customizationType");
        if (customizationType == null) {
            throw new IllegalArgumentException("customizationType must be specified");
        }
        if (customizationType.equals("new-named-family")) {
            NamedFamilyList fontFamily = FontListParser.readNamedFamily(
                    parser, fontDir, updatableFontMap, false);
            if (fontFamily != null) {
                out.add(fontFamily);
            }
        } else {
            throw new IllegalArgumentException("Unknown customizationType=" + customizationType);
        }
    }

    private static void readFamilyList(
            @NonNull XmlPullParser parser,
            @NonNull String fontDir,
            @NonNull List<NamedFamilyList> out,
            @Nullable Map<String, File> updatableFontMap)
            throws XmlPullParserException, IOException {
        final String customizationType = parser.getAttributeValue(null, "customizationType");
        if (customizationType == null) {
            throw new IllegalArgumentException("customizationType must be specified");
        }
        if (customizationType.equals("new-named-family")) {
            NamedFamilyList fontFamily = FontListParser.readNamedFamilyList(
                    parser, fontDir, updatableFontMap, false);
            if (fontFamily != null) {
                out.add(fontFamily);
            }
        } else {
            throw new IllegalArgumentException("Unknown customizationType=" + customizationType);
        }
    }
}
