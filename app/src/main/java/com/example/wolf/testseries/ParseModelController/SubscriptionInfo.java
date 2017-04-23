package com.example.wolf.testseries.ParseModelController;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Arrays;
import java.util.List;

/**
 * Created by WOLF on 19-04-2015.
 */
@ParseClassName("SubscriptionInfo")
public class SubscriptionInfo extends ParseObject
{
    public SubscriptionInfo()
    {
        super();
    }

    public SubscriptionInfo(String userId, String subscriptionDate, int subscriptionDays)
    {
        super();
        setUserId(userId);
        setSubscriptionDate(subscriptionDate);
        setSubscriptionDays(subscriptionDays);
    }

    public SubscriptionInfo(String subscriptionDate, int subscriptionDays, int subjectId)
    {
        super();
        setSubscriptionDate(subscriptionDate);
        setSubscriptionDays(subscriptionDays);
        setSubjectId(subjectId);
    }

    public SubscriptionInfo(String userId, String subscriptionDate, int subscriptionDays, int subjectId)
    {
        super();
        setUserId(userId);
        setSubscriptionDate(subscriptionDate);
        setSubscriptionDays(subscriptionDays);
        setSubjectId(subjectId);
    }

    public String getSubscriptionDate()
    {
        return getString("subscriptionDate");
    }

    public void setSubscriptionDate(String subscriptionDate)
    {
        put("subscriptionDate", subscriptionDate);
    }

    public int getSubscriptionDays()
    {
        return getInt("subscriptionDays");
    }

    public void setSubscriptionDays(int subscriptionDays)
    {
        put("subscriptionDays", subscriptionDays);
    }

    public String getUserId()
    {
        return getString("userId");
    }

    public void setUserId(String userId)
    {
        put("userId", userId);
    }

    public Boolean getIsSubscribedFree()
    {
        return getBoolean("isSubscribedFree");
    }

    public void setIsSubscribedFree(Boolean isSubscribedFree)
    {
        put("isSubscribedFree", isSubscribedFree);
    }

   public int getSubjectId()
   {
       return getInt("subjectId");
   }
    public void setSubjectId(int subjectId)
    {
        put("subjectId", subjectId);
    }



}
