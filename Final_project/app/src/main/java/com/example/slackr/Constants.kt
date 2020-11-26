package com.example.slackr

object Constants {

    fun getQuestions(): ArrayList<Question>{
        val questionList = ArrayList<Question>()

        var que= Question(1, "You prefer to:", "Handle physical objects and try to understand how they work for yourself", "Analyse pictures, graphs and charts", "Listen to things rather than read about them")
        questionList.add(que)
        que= Question(2, "You remember things by:", "Experiencing them for yourself (being hands on", "Watching a demonstration","Saying them out loud")
        questionList.add(que)
        que= Question(3, "You find reading:", "Pretty boring; you'd rather be outside", "The best and most relaxing thing ever","Takes too long; you get fidgety")
        questionList.add(que)
        que= Question (4, "You're more likely to remember somebody's:", "Hug", "Face","Name")
        questionList.add(que)
        que= Question (5,"When you see the word \"cat\", what do you do?:","Think about being with a cat (stroking it or hearing it meow)","Picture a cat in your mind","Say the word \"cat\" to yourself")
        questionList.add(que)
        que= Question(6, " What kind of book would you like to read for fun?:","A book with word searches or crossword puzzle","A book with lots of images","A book with lots of words and details")
        questionList.add(que)
        que = Question(7, "If you're ever unsure of how to spell a word, what are you most likely to do?:", "Trace the letters in the air with your finger","Write it down to see if it looks right", "Spell it out to see if it sounds right")
        questionList.add(que)
        que = Question(8, "You're out shopping and you are standing in the queue at the checkout. What are you most likely to do while you are waiting?:","Fidget, move about or rock / lean on your feet","Look around at other clothes","Talk to the person next to you in the queue")
        questionList.add(que)
        que= Question(9,"What's the best way for you to study for an exam?:","Make index cards that you can review","Read the book or your notes and review pictures or charts","Get a friend or family member to ask you questions that you can answer out loud")
        questionList.add(que)
        que=Question(10,"What do you like to do to relax?:","Exercise (walk, run, play sports, etc.)","Read","Listen to music")
        questionList.add(que)
        return questionList
    }

}