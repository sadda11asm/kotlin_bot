package bot

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.ivmg.telegram.bot
import me.ivmg.telegram.dispatch
import me.ivmg.telegram.dispatcher.command
import me.ivmg.telegram.dispatcher.text
import me.ivmg.telegram.network.fold
import java.lang.Double.parseDouble
import java.util.Arrays




fun main(args: Array<String>) {
    val bot = bot {
        token = "850140188:AAEWX5X43-ZqCplmN6jnkpnd0i3W9QOQFuY"
        dispatch {
            text { bot, update ->
                val text = (update.message?.text ?: "Hello, World!")
                if (text[0]=='/') return@text
                val count = analyzeText(text)
                if (count.toInt() == -1) {
                    bot.sendMessage(chatId = update.message!!.chat.id, text = "Not correct time given!")
                } else {
                    GlobalScope.launch {
                        delay(count)
                        bot.sendMessage(chatId = update.message!!.chat.id, text = "$text have gone!")
                    }
                }
            }
        }

        dispatch {
            command("start") { bot, update ->
                val result = bot.sendMessage(chatId = update.message!!.chat.id, text = "Hi there!")
            }
        }
    }
    bot.startPolling()
}

fun analyzeText(text: String): Long {
    if (isNumber(text)) return text.toLong()*1000
    val str = text.split(' ')
    if (isNumber(str[0])) {
        return str[0].toLong()* convertToNumeric("one " + str[1])
    }
    return convertToNumeric(text)
}

fun convertToNumeric(text: String): Long {
    var input = text
    var isValidInput = true
    var result: Long = 0
    var finalResult: Long = 0
    val allowedStrings = Arrays.asList(
        "zero", "one", "two", "three", "four", "five", "six", "seven",
        "eight", "nine", "ten", "eleven", "twelve", "thirteen", "fourteen",
        "fifteen", "sixteen", "seventeen", "eighteen", "nineteen", "twenty",
        "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety",
        "hundred", "thousand", "million", "billion", "trillion"
    )
    var sec = 1
    var min = 0
    var hour = 0
    if (input != null && input.length > 0) {
        input = input.replace("-".toRegex(), " ")
        input = input.toLowerCase().replace(" and".toRegex(), " ")
        val splittedParts =
            input.trim { it <= ' ' }.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        var ok = 0
        for (str in splittedParts) {
            if (!allowedStrings.contains(str)) {
                //isValidInput = false
                if ((str.equals("seconds") || str.equals("second")) && ok==0) {
                    sec = 1
                    ok = 1
                } else if ((str.equals("minutes") || str.equals("minute")) && ok==0) {
                    sec = 0
                    min = 1
                    ok = 1
                } else if ((str.equals("hours") || str.equals("hour")) && ok==0) {
                    sec = 0
                    hour = 1
                    ok = 1
                } else {
                    return -1
                }
            }
        }
            for (str in splittedParts) {
                if (str.equals("zero", ignoreCase = true)) {
                    result += 0
                } else if (str.equals("one", ignoreCase = true)) {
                    result += 1
                } else if (str.equals("two", ignoreCase = true)) {
                    result += 2
                } else if (str.equals("three", ignoreCase = true)) {
                    result += 3
                } else if (str.equals("four", ignoreCase = true)) {
                    result += 4
                } else if (str.equals("five", ignoreCase = true)) {
                    result += 5
                } else if (str.equals("six", ignoreCase = true)) {
                    result += 6
                } else if (str.equals("seven", ignoreCase = true)) {
                    result += 7
                } else if (str.equals("eight", ignoreCase = true)) {
                    result += 8
                } else if (str.equals("nine", ignoreCase = true)) {
                    result += 9
                } else if (str.equals("ten", ignoreCase = true)) {
                    result += 10
                } else if (str.equals("eleven", ignoreCase = true)) {
                    result += 11
                } else if (str.equals("twelve", ignoreCase = true)) {
                    result += 12
                } else if (str.equals("thirteen", ignoreCase = true)) {
                    result += 13
                } else if (str.equals("fourteen", ignoreCase = true)) {
                    result += 14
                } else if (str.equals("fifteen", ignoreCase = true)) {
                    result += 15
                } else if (str.equals("sixteen", ignoreCase = true)) {
                    result += 16
                } else if (str.equals("seventeen", ignoreCase = true)) {
                    result += 17
                } else if (str.equals("eighteen", ignoreCase = true)) {
                    result += 18
                } else if (str.equals("nineteen", ignoreCase = true)) {
                    result += 19
                } else if (str.equals("twenty", ignoreCase = true)) {
                    result += 20
                } else if (str.equals("thirty", ignoreCase = true)) {
                    result += 30
                } else if (str.equals("forty", ignoreCase = true)) {
                    result += 40
                } else if (str.equals("fifty", ignoreCase = true)) {
                    result += 50
                } else if (str.equals("sixty", ignoreCase = true)) {
                    result += 60
                } else if (str.equals("seventy", ignoreCase = true)) {
                    result += 70
                } else if (str.equals("eighty", ignoreCase = true)) {
                    result += 80
                } else if (str.equals("ninety", ignoreCase = true)) {
                    result += 90
                } else if (str.equals("hundred", ignoreCase = true)) {
                    result *= 100
                } else if (str.equals("thousand", ignoreCase = true)) {
                    result *= 1000
                    finalResult += result
                    result = 0
                } else if (str.equals("million", ignoreCase = true)) {
                    result *= 1000000
                    finalResult += result
                    result = 0
                } else if (str.equals("billion", ignoreCase = true)) {
                    result *= 1000000000
                    finalResult += result
                    result = 0
                } else if (str.equals("trillion", ignoreCase = true)) {
                    result *= 1000000000000L
                    finalResult += result
                    result = 0
                }
            }

            finalResult += result
            result = 0
            if (sec==1) finalResult*=1000
            else if (min==1) finalResult*=60000
            else if (hour==1) finalResult*=3600000
            return finalResult
    }
    return 0
}

fun isNumber(text: String): Boolean {
    var numeric = true
    try {
        parseDouble(text)
    } catch (e: NumberFormatException) {
        numeric = false
    }
    return numeric
}
