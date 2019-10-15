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
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
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
        ArrayList<BComponent> points = resolve (
                BString.make(
                        SlotPath.unescape("slot:|bql: select * from control:ControlPoint")));

        BTagImporter tagImporter =
                ((BResoluteTagUtils)Sys.getService(BResoluteTagUtils.TYPE)).getTagImporter();

        multvsearch remover = (String filter[], BComponent point) -> {
            log().message("Begin Bulk tag remove operation...");
            if(filter[0].equals("*")){
                point.tags().forEach( tag -> {
                    boolean removed = point.tags().remove(tag);
                    String msg = "Removed ".concat(tag.getId().getQName()) +
                            " - ".concat(String.valueOf(removed));
                    log().message(msg);
                    logger.info(msg);
                });
            }else{
                for(String f : filter){
                    AtomicBoolean removed = new AtomicBoolean(false);
                    point.tags()
                            .filter(tag -> tag.getId().getName().equals(f))
                            .forEach(t -> {
                                removed.set(point.tags().remove(t));
                                String msg = "removed tag ".concat(t.getId().getQName()) +
                                        "\n\t\tfrom ".concat(point.getName() +
                                                        "\n\t\tfiltered by ".concat(f) +
                                                        "\n\t\t".concat(String.valueOf(removed)));
                                logger.info(msg);
                                log().message(msg);
                            });
                    String msg = "Removed tag? ".concat(String.valueOf(removed));
                    logger.info(msg);
                    log().message(msg);

                }
            }
        };

        if(tagImporter.getTagFilter().isEmpty()){
            logger.severe("[Bulk Tag Remove Async Job ERROR] - tagFilter field is empty; canceling job...!");
            this.cancel();
        }else{
            points.forEach( point -> {
                String[] filter = tagImporter.getTagFilter().split(",");
                String msg = point.getName() + tagImporter.getTagFilter();
                remover.remove(filter, point);
                logger.info(msg);
                log().message(msg);
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

    @FunctionalInterface()
    interface multvsearch { void remove(String[] filter, BComponent point); }

}
