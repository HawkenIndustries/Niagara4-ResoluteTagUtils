package com.resolute.ResoluteTagUtils.jobs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import com.resolute.ResoluteTagUtils.components.BPointTable;
import com.resolute.ResoluteTagUtils.components.BTagImporter;
import com.resolute.ResoluteTagUtils.models.Point;
import com.resolute.ResoluteTagUtils.services.BResoluteTagUtils;

import javax.baja.file.BIFile;
import javax.baja.job.BSimpleJob;
import javax.baja.naming.BOrd;
import javax.baja.naming.SlotPath;
import javax.baja.naming.UnresolvedException;
import javax.baja.nre.annotations.NiagaraType;
import javax.baja.sys.*;
import javax.baja.tag.TagDictionary;
import javax.baja.tag.TagDictionaryService;
import javax.baja.tag.TagInfo;

import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.logging.Logger;

@NiagaraType

public class BTaggingJob extends BSimpleJob {
/*+ ------------ BEGIN BAJA AUTO GENERATED CODE ------------ +*/
/*@ $com.resolute.ResoluteTagUtils.jobs.BTaggingJob(2979906276)1.0$ @*/
/* Generated Fri Oct 11 09:13:08 EDT 2019 by Slot-o-Matic (c) Tridium, Inc. 2012 */

////////////////////////////////////////////////////////////////
// Type
////////////////////////////////////////////////////////////////
  
  @Override
  public Type getType() { return TYPE; }
  public static final Type TYPE = Sys.loadType(BTaggingJob.class);

/*+ ------------ END BAJA AUTO GENERATED CODE -------------- +*/

    private static Logger logger = Logger.getLogger("Resolute Tag Utils");
    private Gson gson = new GsonBuilder()
            .serializeNulls()
            .setPrettyPrinting()
            .create();

    @Override
    public void run(Context cx) throws Exception {

        TagDictionaryService tagDictionaryService = Sys.getStation().getTagDictionaryService();

        BTagImporter tagImporter =
                ((BResoluteTagUtils)Sys.getService(BResoluteTagUtils.TYPE)).getTagImporter();

        HashSet<Point> points = AccessController.doPrivileged(
                (PrivilegedAction<HashSet<Point>>)() -> {
                    try {
                        JsonObject json = gson.fromJson( new String(
                                ((BIFile)tagImporter.getImportFile().get(this))
                                        .read()) , JsonObject.class);

                        /***
                         * pass primitives right to parent component as properties.
                         */
                        tagImporter.setJsonVersion(json.get("version").getAsDouble());
                        JsonArray currentTagSet = json.get("currentTagSet").getAsJsonArray();
                        StringBuilder sb = new StringBuilder();
                        currentTagSet.forEach( tag -> {
                            sb.append(tag);
                        });
                        tagImporter.setTags(sb.toString());

                        HashSet<Point> pointList = new HashSet<>();
                        JsonArray jsonPoints = json.get("points").getAsJsonArray();
                        jsonPoints.forEach( p ->{
                            String pstr = gson.toJson(p.getAsJsonObject());
                            pointList.add(gson.fromJson(pstr, Point.class));
                        });
                        return pointList;

                    } catch (IOException ioe) {
                        logger.severe(ioe.getMessage());
                        log().message(ioe.getMessage());
                        ioe.printStackTrace();
                    } catch(UnresolvedException ue){
                        logger.severe(ue.getMessage());
                        log().message(ue.getMessage());
                    } catch(Exception e){
                        logger.severe(e.getMessage());
                        e.printStackTrace();
                    }
                    return null;
                });

        /***
         * Pass the hashset to a baja table for the ui to display values...
         */
        if(points != null){
            BPointTable table = BPointTable.make();
            table.set(points);
            tagImporter.setPointTable(table);
        }

        /***
         * On-Demand implementation of the functional interface multvsearch.
         * Iterates over all tags in every dictionary, checking every rbi tag
         * for every rbi point in the list.
         */

        Collection<TagDictionary> tagDictionaries = tagDictionaryService.getTagDictionaries();

        multvsearch searcher = (TagInfo tagInfo, HashSet<Point> pointList) -> {
            log().message("dictionary tag: ".concat(tagInfo.getName()));
            pointList.forEach(point -> {
                log().message("Resolute Point: ".concat(point.getName()));
                for(String tag:point.getTags()){
                    log().message("Resolute Tag: ".concat(tag));
                    if(tagInfo.getName().equals(tag)){
                        try{
                            log().message("\t\tFound Matching Tag...!!!");
                            String relPath = "slot:".concat(
                                    (point.getMetricId().split("\\."))[1] );
                            BOrd ord = BOrd.make(SlotPath.unescape(relPath));
                            tagInfo.setTagOn((BComponent) ord.get(Sys.getStation()));

                        }catch(UnresolvedException ue){
                            log().message(ue.getMessage());
                            logger.severe(ue.getMessage());
                        }
                    }
                }
            });
        };

        log().message("Dictionaries found: ");

        for (TagDictionary tagDictionary : tagDictionaries) {
            final String msg = tagDictionary.getDisplayName(null);
            log().message(msg);
            logger.info(msg);

            if(points != null){
                if(!points.isEmpty()){
                    Iterator<TagInfo> tagInfoIterator = tagDictionary.getTags();
                    tagInfoIterator.forEachRemaining(tagInfo -> {
                        searcher.search(tagInfo, points);
                    });
                }else{
                    log().message("Parsed empty list out of json file...");
                    logger.severe("Parsed empty list out of json file...!!!");
                }
            }else{
                log().message("Parsed a null list out of json file...!!!");
                logger.severe("Parsed a null list out of json file...!!!");
            }

        }

    }

    @FunctionalInterface
    interface multvsearch { void search(TagInfo tagInfo, HashSet<Point> points); }
}
