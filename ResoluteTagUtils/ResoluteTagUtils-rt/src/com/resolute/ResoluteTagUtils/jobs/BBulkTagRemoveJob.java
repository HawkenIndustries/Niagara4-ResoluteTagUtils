package com.resolute.ResoluteTagUtils.jobs;

import com.resolute.ResoluteTagUtils.components.BTagImporter;
import com.resolute.ResoluteTagUtils.services.BResoluteTagUtils;

import javax.baja.collection.BITable;
import javax.baja.collection.TableCursor;
import javax.baja.job.BSimpleJob;
import javax.baja.naming.BOrd;
import javax.baja.naming.SlotPath;
import javax.baja.nre.annotations.NiagaraType;
import javax.baja.sys.*;
import javax.baja.tag.TagDictionary;
import javax.baja.tag.TagDictionaryService;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

@NiagaraType

public class BBulkTagRemoveJob extends BSimpleJob {
/*+ ------------ BEGIN BAJA AUTO GENERATED CODE ------------ +*/
/*@ $com.resolute.ResoluteTagUtils.jobs.BBulkTagRemoveJob(2979906276)1.0$ @*/
/* Generated Tue Oct 15 10:14:09 EDT 2019 by Slot-o-Matic (c) Tridium, Inc. 2012 */

////////////////////////////////////////////////////////////////
// Type
////////////////////////////////////////////////////////////////

    /***
     * Filter semantics:
     *      "*" = remove all tags from all points and all tag-dictionary (Exception: Implied tags and smart tags will remain)
     *      ""  = will produce a null pointer runtime exception
     *      "abc,abc,abc" = will produce an array of resolute tags to match and remove from all points and all matching
     *      dictionaries.
     */
  
  @Override
  public Type getType() { return TYPE; }
  public static final Type TYPE = Sys.loadType(BBulkTagRemoveJob.class);

/*+ ------------ END BAJA AUTO GENERATED CODE -------------- +*/

    private static Logger logger = Logger.getLogger("Resolute Tag Utils");

    @Override
    public void run(Context context) throws Exception {

        BTagImporter tagImporter =
                ((BResoluteTagUtils)Sys.getService(BResoluteTagUtils.TYPE)).getTagImporter();

        progress(0);
        logger.info("Bulk-Tag-Remove-Job Progress...".concat(String.valueOf(getProgress())));

        ArrayList<BComponent> points = resolve (
                BString.make(
                        SlotPath.unescape("slot:|bql: select * from control:ControlPoint")));
        TagDictionaryService tagDictionaryService = Sys.getStation().getTagDictionaryService();
        Collection<TagDictionary> tagDictionaries = tagDictionaryService.getTagDictionaries();

        if(tagImporter.getDeleteFilter().isEmpty()){
            logger.severe("[Bulk Tag Remove Async Job ERROR] - tagFilter field is empty; canceling job...!");
            this.cancel();
        }else{
            String[] filters = tagImporter.getDeleteFilter().split(",");
            progress(1);
            logger.info("Bulk-Tag-Remove-Job Progress...".concat(String.valueOf(getProgress())));
            points.forEach( point -> {
                if(filters[0].equals("*")){

                    final long before, after, elapsed;
                    AtomicReference<Integer> totalOps = new AtomicReference<>(0);
                    long progressUnitPerPoint = 100 / points.size();
                    before = System.currentTimeMillis();

                    point.tags().forEach( tag -> {

                        ///////////BENCHMARK////////////////////////////////////////////////////////////
                        totalOps.set(totalOps.get() + point.tags().getAll().size());
                        long progressUnit = progressUnitPerPoint / point.tags().getAll().size();
                        if(getProgress() < 100){
                            int progress = 0;
                            progress(progress+=progressUnit);
                        }
                        ///////////BENCHMARK////////////////////////////////////////////////////////////
                        boolean removed = point.tags().remove(tag);
                        String msg = "Removed ".concat(tag.getId().getQName()) +
                                " - ".concat(String.valueOf(removed)) +
                                " - Progress...".concat(String.valueOf(getProgress()));
                        log().message(msg);
                        logger.info(msg);
                    });

                    ///////////BENCHMARK////////////////////////////////////////////////////////////
                    after = System.currentTimeMillis();
                    elapsed = after - before;
                    logger.info("N of Points: ".concat(String.valueOf(points.size())));
                    logger.info("Start Time: ".concat(String.valueOf(before)));
                    logger.info("End Time: ".concat(String.valueOf(after)));
                    logger.info("------------");
                    logger.info("Elapsed Time: ".concat(String.valueOf(elapsed)));
                    logger.info("Total Ops: ".concat(String.valueOf(totalOps.get())));
                    logger.info("Time elapsed Per Operation: "
                            .concat(String.valueOf((double) elapsed / (double)totalOps.get())) + " millis");
                }else{
                    filterDictionaries(filters, tagDictionaries, point);
                    filterTags(filters, point);
                }
            });
        }
    }

    @SuppressWarnings("unchecked")
    public ArrayList<BComponent> resolve(BString query){
        ArrayList<BComponent> objRefs = new ArrayList<>();
        BOrd ord = BOrd.make(query.getString());
        BITable<BComponent> t = (BITable)ord.resolve(Sys.getStation()).get();
        TableCursor<BComponent> c = t.cursor();
        while(c.next()){
            try{
                objRefs.add(c.get());
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return objRefs;
    }

    private void filterTags(String[]filter, BComponent point) {
        for(String f : filter){
            point.tags().forEach( tag -> {
                logger.info("tag: ".concat(tag.getId().getName()) +
                        "\t-\tfilter: ".concat(f));
                if(tag.getId().getName().equals(f)){
                    boolean removed = point.tags().remove(tag);
                    String msg = "Removed ".concat(tag.getId().getQName()) +
                            " - ".concat(String.valueOf(removed));
                    log().message(msg);
                    logger.info(msg);
                }
            });
        }
    }

    private void filterDictionaries(String[] filter,
                                   Collection<TagDictionary> tagDictionaries,
                                   BComponent point) {
        for(String f : filter){
            tagDictionaries.forEach( tagDictionary -> {
                if(tagDictionary.getDisplayName(null).equals(f)){
                    logger.info("Dictionary: ".concat(tagDictionary.getDisplayName(null)) +
                                "\t-\tfilter:  ".concat(f));
                    point.tags().forEach( tag -> {
                        logger.info("Tag's dictionary ".concat(tag.getId().getDictionary()));

                        if(tag.getId().getDictionary()
                                .equals(tagDictionary.getNamespace())){
                            boolean removed = point.tags().remove(tag);

                            logger.info("Id: ".concat(tag.getId().toString()) +
                                    "\nNamespace: ".concat(tagDictionary.getNamespace()) +
                                    "\nRemoved "+removed);
                            log().message("Id: ".concat(tag.getId().toString()) +
                                    "\nNamespace: ".concat(tagDictionary.getNamespace()) +
                                    "\nRemoved "+removed);
                        }
                    });
                }
            });
        }
    }
}
