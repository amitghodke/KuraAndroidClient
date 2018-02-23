package amittestapp.admin.example.com.tcpconnectdemo;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import org.eclipse.kapua.gateway.client.Application;
import org.eclipse.kapua.gateway.client.Client;
import org.eclipse.kapua.gateway.client.ErrorHandler;
import org.eclipse.kapua.gateway.client.Errors;
import org.eclipse.kapua.gateway.client.MessageHandler;
import org.eclipse.kapua.gateway.client.Payload;
import org.eclipse.kapua.gateway.client.Sender;
import org.eclipse.kapua.gateway.client.Topic;
import org.eclipse.kapua.gateway.client.Transport;
import org.eclipse.kapua.gateway.client.mqtt.fuse.FuseClient;
import org.eclipse.kapua.gateway.client.profile.kura.KuraMqttProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;

import static org.eclipse.kapua.gateway.client.Credentials.userAndPassword;
import static org.eclipse.kapua.gateway.client.Errors.ignore;
import static org.eclipse.kapua.gateway.client.Transport.waitForConnection;
@RequiresApi(api = Build.VERSION_CODES.N)

public class KapuaConnectDemoActivity extends AppCompatActivity {
    private static final Logger logger = LoggerFactory.getLogger(KapuaConnectDemoActivity.class);
    public static final String android_client="Micromax A106";
    public static final String HOST= "tcp://<server_ip>";
    public static final String port="<port>";
    public static final String account_name="<account_name>";
    public static final String username=    "<username>";
    public static final String password="password";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kapua_connect);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            Connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public  void Connect() throws Exception {
        try {
            final Client client = KuraMqttProfile.newProfile(FuseClient.Builder::new)
                    .accountName("<account_name>")
                    .clientId(android_client)
                    .brokerUrl("tcp://server_ip_port>")
                    .credentials(userAndPassword("username", "password"))
                    .build();

            Application.Builder builder = client.buildApplication("location");
            final Application application = builder.build();
            // wait for connection

            Transport. waitForConnection(application.transport());
            // subscribe to a topic
            application.data(Topic.of("Android Data", "Location")).subscribe(
                    new MessageHandler() {
                        @Override
                        public void handleMessage(Payload message) {
                            System.out.format("Received: %s%n", message);
                        }
                    });


            // example payload
            try{
                final Payload.Builder payload = new Payload.Builder();
                payload.put("latitude", "1.978");
                payload.put("longitude", 1.3564);
                // send, handling error ourself
                application.data(Topic.of("Android Data", "Location")).send(payload);
                // send, with attached error handler
                try {
                    application.data(Topic.of("Android Data", "Location"))
                            .errors(
                                    new ErrorHandler<Throwable>() {
                                        @Override
                                        public void handleError(Throwable e, Optional<Payload> payload) throws Throwable {
                                            System.err.println("Failed to publish: " + e.getMessage());
                                        }
                                    }
                            )
                            //handle((e, message) -> System.err.println("Failed to publish: " + e.getMessage())))
                            .send(payload);
                } catch (Exception throwable) {
                    throwable.printStackTrace();
                }
                catch ( Throwable throwable) {
                    throwable.printStackTrace();
                }

//                // ignoring error
                application.data(Topic.of("Android Data", "Location")).errors(ignore()).send(payload);
                // cache sender instance

                final Sender<RuntimeException> sender = application.data(Topic.of("Android Data", "Location")).errors(ignore());

                int i = 0;
                while (i < 10) {
                    // send
                    sender.send(Payload.of("counter", i++));
                    Thread.sleep(1_000);
                }

                // sleep to not run into Paho thread starvation
                Thread.sleep(100_000);

                Thread.sleep(1_000);
            }
            catch(Exception e )
            {

                e.printStackTrace();
            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
//try (Client client = KuraMqttProfile.newProfile(FuseClient.Builder::new)
//                .accountName("kapua-sys")
//                .clientId(android_client)
//                .brokerUrl(HOST+":"+port)
//                .credentials(userAndPassword("kapua-broker", "kapua-password"))
//                .build()) {
//
//            try (Application application = client.buildApplication("android").build()) {
//                Sender<RuntimeException> sender = application
//                        .data(Topic.of("Android Data", "Location"))
//                        .errors(ignore());
//
//
//                logger.info("Waiting for connection..");
//                // wait for connection
//                waitForConnection(application.transport());
//
//                logger.info("Waiting for connection.. Done !...");
//                // subscribe to a topic
//
//                int i = 0;
//                while (true) {
//                    // send
//                    sender.send(Payload.of("counter", i++));
//                    Thread.sleep(1000);
//                }
//            }
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
