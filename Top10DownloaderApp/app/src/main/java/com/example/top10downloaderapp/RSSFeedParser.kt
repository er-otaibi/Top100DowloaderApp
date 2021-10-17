package com.example.top10downloaderapp

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory

class RSSFeedParser {
    private val TAG = "FeedParser"
    private val applications = ArrayList<FeedEntry>()

    fun parse(xmlData: String): Boolean {

        var status = true
        var inEntry = false
        var textValue = ""

        try {
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val xpp = factory.newPullParser()
            xpp.setInput(xmlData.reader())
            var eventType = xpp.eventType
            var currentRecord = FeedEntry()
            while (eventType != XmlPullParser.END_DOCUMENT) {

                val tagName = xpp.name?.toLowerCase()

                when (eventType) {

                    XmlPullParser.START_TAG -> {
                        if (tagName == "entry") {
                            inEntry = true
                        }
                    }

                    XmlPullParser.TEXT -> textValue = xpp.text

                    XmlPullParser.END_TAG -> {

                        if (inEntry) {
                            when (tagName) {
                                "entry" -> {
                                    applications.add(currentRecord)
                                    inEntry = false
                                    currentRecord = FeedEntry()   // create a new object
                                }

                                "name" -> currentRecord.name = textValue

                            }
                        }


                    }
                }

                eventType = xpp.next()

            }

        } catch (e: Exception) {
            e.printStackTrace()
            status = false
        }

        return status
    }

    fun getParsedList(): ArrayList<FeedEntry> {

        return applications
    }
}