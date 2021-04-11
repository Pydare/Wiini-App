import html
import io
import os
from google.api_core import operation

# Imports the google cloud client libraries
from google.api_core.exceptions import AlreadyExists
from google.cloud import texttospeech
from google.cloud import translate_v3beta1 as translate
from google.cloud import vision

PROJECT_ID = os.environ["GCLOUD_PROJECT"]

def pic_to_text(infile: str) -> str:
    """Dectects text in an image file

    ARGS
    infile: path to image file

    RETURNS
    String of text detected in image
    """
    # Instantiates a client
    client = vision.ImageAnnotatorClient()

    # Opens the input image file
    with io.open(infile, "rb") as image_file:
        content = image_file.read()

    image = vision.Image(content=content)

    # for dense text, use docuement_text_detection
    # for less dense text, use text_detection
    response = client.document_text_detection(image=image)
    text = response.full_text_annotation.text
    print("Detected text: {}".format(text))

    return text


def create_gloassary(languages, project_id, gloassary_name, glossary_uri):
    """Creates a GCP glossary resource
    Assumes you've already manually uploaded a glossary to Cloud Storage

    ARGS
    languages: list of languages in the glossary
    project_id: GCP project id
    glossary_name: name you want to give this glossary resource
    glossary_uri: the uri of the glossary you uploaded to Cloud storage

    RETURNS
    nothing
    """
    # Instantiates a client
    client = translate.TranslationServiceClient()

    # Designates the data center location that you want to use
    location = "us-central1"

    # set glossary resource name
    name = client.glossary_path(project_id, location, gloassary_name)

    # set language codes
    language_codes_set = translate.Glossary.LanguageCodesSet(
        language_codes=languages
    )

    gcs_source = translate.GcsSource(input_uri=glossary_uri)

    input_config = translate.GlossaryInputConfig(gcs_source=gcs_source)

    # set glossary resource information
    glossary = translate.Glossary(
        name=name, language_codes_set=language_codes_set, input_config=input_config
    )

    parent = f"projects/{project_id}/locations/{location}"

    # create glossary resource
    # handle exception for case in which a glossary
    # with glossary_name already exists
    try:
        operation = client.create_glossary(parent=parent, glossary=glossary)
        operation.result(timeout=90)
        print("Created glossary " + gloassary_name + ".")
    except AlreadyExists:
        print(
            "The glossary "
            + gloassary_name
            + " already exists. No new glossary was created"
        )


def translate_text(text, source_language_code, target_language_code, project_id, glossary_name):
    """Translates text to a given language using a glossary

    ARGS
    text: String of text to translate
    source_language_code: language of input text
    target_language_code: language of output text
    project_id: GCP project id
    gloassary_name: name you gave project's glossary resource when created

    RETURNS
    String of translated text
    """

    # Instantiates a client
    client = translate.TranslationServiceClient()

    # Designates the data center location that you want to use
    location = "us-central1"

    glossary = client.glossary_path(project_id, location, glossary_name)

    glossary_config = translate.TranslateTextGlossaryConfig(glossary=glossary)

    parent = f"projects/{project_id}/locations/{location}"

    result = client.translate_text(
        request={
            "parent": parent,
            "contents": [text],
            "mime_type": "text/plain",  # mime types: text/plain, text/html
            "source_language_code": source_language_code,
            "target_language_code": target_language_code,
            "glossary_config": glossary_config,
        }
    )

    # Extract translated text from API response
    return result.glossary_translations[0].translated_text


def text_to_speech(text, outfile):
    """Converts plaintext to SSML and
    generates synthetic audio from SSML

    ARGS
    text: text to synthesize
    outfile: filename to use to store synthetic audio

    RETURNS
    nothing
    """

    # replace special characters with HTML Ampersand character codes
    # these codes prevent the API  from confusing text with
    # SSML commands
    # for example, '<' --> '&lt; and '&' --> '&amp'
    escaped_lines = html.escape(text)

    # convert plaintext to SSML in order to wait two seconds
    # between each line in synthetic speech
    ssml = "<speak>{}</speak>".format(
        escaped_lines.replace("\n", '\n<break time="2s"/>')
    )

    # Instantiates a client
    client = texttospeech.TextToSpeechClient()

    # sets the text input to be synthesized
    synthesis_input = texttospeech.SynthesisInput(ssml=ssml)

    # builds the voice request, selects the language code (en-US) and
    # the SSML voice gender (MALE)
    voice = texttospeech.VoiceSelectionParams(
        language_code="en-US", ssml_gender=texttospeech.SsmlVoiceGender.MALE
    )

    # seletct the type of audio file to return
    audio_config = texttospeech.AudioConfig(
        audio_encoding=texttospeech.AudioEncoding.MP3
    )

    # performs the text-to-speech request on the text input 
    # with the selected voice params and audio file type
    request = texttospeech.SynthesizeSpeechRequest(
        input=synthesis_input, voice=voice, audio_config=audio_config
    )

    response = client.synthesize_speech(request=request)

    # writes the synthetic audio to the output file
    with open(outfile, "wb") as out:
        out.write(response.audio_content)
        print("Audio content writtent to the fle " + outfile)



def main():

    # photo from which to extract text
    infile = "resources/example.png"
    # name of file that will hold synthetic speech
    outfile = "resources/example.mp3"

    # defines the languages in the glossary
    # this list must match the languages in the glossary
    # here, the glossary includes english and french
    glossary_langs = ["fr", "en"]
    # name that will be assigned to your project's glossary resource
    glossary_name = "translation_bistro_glossary"
    # uri of .csv file uploaded to Cloud Storage
    glossary_uri = "gs://my_tts_glossary_bucket/{}.csv".format(glossary_name)

    create_gloassary(glossary_langs, PROJECT_ID, glossary_name, glossary_uri)

    # photo -> detect text
    text_to_translate = pic_to_text(infile)
    # detected text -> translated text
    text_to_speak = translate_text(
        text_to_translate, "fr", "en", PROJECT_ID, glossary_name
    )
    # translated text -> synthetic audio
    text_to_speech(text_to_speak, outfile)


main()