package Cache;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

public class APISever extends Application {

    /**
     * Creates a root Restlet that will receive all incoming calls.
     */
    @Override
    public synchronized Restlet createInboundRoot() {
        // Create a router Restlet that routes each call to a new instance of Resource.
        Router router = new Router(getContext());

        // Defines routes
        router.attach("/files", FileList.class);
        router.attach("/file", Document.class);
        router.attach("/log", Log.class);
        router.attach("/clearcache", CachedFiles.class);

        return router;
    }

}
